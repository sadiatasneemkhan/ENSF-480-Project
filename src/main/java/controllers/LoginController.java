package controllers;

import com.mysql.cj.jdbc.JdbcPreparedStatement;
import models.Property;
import models.PropertyForm;
import models.Renter;
import models.User;
import util.DatabaseConnection;
import util.PropertyFormUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LoginController {
    private static final LoginController loginController = new LoginController();

    private final Connection connection = DatabaseConnection.getConnection();

    private User currentUser = null;

    private LoginController() { }

    public Optional<User> login(final String email, final String password) {
        try {
            if (!checkUserExists(email)) {
                throw new RuntimeException("User " + email + " does not exist.");
            }

            final String query = "SELECT * FROM user WHERE email = ? AND password = ? LIMIT 1";
            final PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);

            final ResultSet rs = ps.executeQuery();
            final Optional<User> user = User.createFromResultSet(rs);
            if (user.isPresent()) {
                System.out.println("Successfully logged in " + user.toString());

                User updatedUser = user.get();

                if (updatedUser instanceof Renter) {
                    persistNotificationsForRenter((Renter) updatedUser);
                }
                updatedUser = updateUserLastLogin(updatedUser).orElse(updatedUser);

                currentUser = updatedUser;
            } else {
                throw new RuntimeException("Error logging " + email + " in.");
            }
        } catch (final SQLException | RuntimeException throwables) {
            throwables.printStackTrace();
        }

        return Optional.of(currentUser);
    }

    private Optional<User> updateUserLastLogin(final User userModel) throws SQLException {
        userModel.setLastLogin(LocalDateTime.now());

        final String query = "UPDATE user SET last_login = ? WHERE id = ? LIMIT 1";
        final PreparedStatement ps = connection.prepareStatement(query);
        ps.setObject(1, userModel.getLastLogin());
        ps.setInt(2, userModel.getId());

        final int updatedRows = ps.executeUpdate();

        if (updatedRows < 1) {
            System.err.println("Error updated user " + userModel.getEmail() + " last login");
            return Optional.of(userModel);
        }

        return fetchUser(userModel);
    }

    private Optional<User> fetchUser(final User userModel) throws SQLException {
        final String query = "SELECT * FROM user WHERE id = ? LIMIT 1";
        final PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, userModel.getId());

        final ResultSet rs = ps.executeQuery();
        return User.createFromResultSet(rs);
    }

    public Optional<User> register(final String email, final String password, final User.UserRole role) throws IllegalArgumentException {
        try {
            if (checkUserExists(email)) {
                throw new IllegalArgumentException("email " + email + " is taken");
            }
        } catch (final SQLException e) {
            e.printStackTrace();
            return null;
        }

        final User user = User.fromRole(role, email, password);
        final String query = "INSERT INTO user(email, password, role) VALUES (?,?,?)";
        try {
            final PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().toString());

            final int row = ps.executeUpdate();
            System.out.println(row + " row(s) affected from register.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return login(email, password);
    }

    public Collection<Property> getNotificationsForRenter(final Renter renter) {
        final List<Property> notifications = new ArrayList<>();
        final String query = "SELECT p.* FROM property p " +
                "INNER JOIN user_notification un ON p.id = un.property_id " +
                "WHERE un.user_id = ?";
        try {
            final PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, renter.getId());

            final ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Property.createFromResultSet(rs).ifPresent(notifications::add);
            }

            clearNotificationsForRenter(renter);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return notifications;
    }

    private void clearNotificationsForRenter(final Renter renter) {
        final String query = "DELETE FROM user_notification WHERE user_id = ?";
        try {
            final PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, renter.getId());

            final int rowsDeleted = ps.executeUpdate();
            System.out.println("DEBUG: " + rowsDeleted + " row(s) deleted from user notifications");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private boolean checkUserExists(final String email) throws SQLException {
        final String query = "SELECT EXISTS(SELECT * FROM user WHERE email = ?)";
        final PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, email);
        final ResultSet rs = ps.executeQuery();

        rs.next();
        return rs.getBoolean(1);
    }

    public Collection<Property> getPropertiesForUser(final PropertyForm form, final LocalDateTime lastLogin) {
        final String query = PropertyFormUtil.getPropertyFilterQuery(form) + " AND date_published > ?";
        return getMatchingPropertiesForUser(query, lastLogin);
    }

    private Collection<Property> getMatchingPropertiesForUser(final String query, final LocalDateTime lastLogin) {
        final Collection<Property> properties = new ArrayList<>();
        try {
            final PreparedStatement ps = connection.prepareStatement(query);
            ps.setObject(1, lastLogin);

            System.out.println("DEBUG: query = " + ((JdbcPreparedStatement) ps).getPreparedSql());

            final ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Property.createFromResultSet(rs).ifPresent(properties::add);
            }

        } catch (final SQLException throwables) {
            throwables.printStackTrace();
        }
        return properties;
    }

    public Optional<PropertyForm> getPropertyForm(final Renter renter) {
        if (renter.getPropertyFormId() == null) {
            return Optional.empty();
        }

        final String query = "SELECT property_form.* FROM property_form " +
                "INNER JOIN user u on property_form.id = u.property_form_id WHERE u.id = ? " +
                "LIMIT 1";
        try {
            final PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, renter.getId());

            final ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("DEBUG: property form fetched for " + renter.getId());
                return PropertyForm.createFromResultSet(rs);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public void persistNotificationsForRenter(final Renter renter) {
        if (renter.getPropertyFormId() == null) {
            return;
        }

        final Optional<PropertyForm> propertyForm = getPropertyForm(renter);
        propertyForm.ifPresent(form -> {
            final Collection<Property> matchingProperties = getPropertiesForUser(form, renter.getLastLogin());
            if (matchingProperties.isEmpty()) {
                return;
            }
            //language=SQL
            String query = "INSERT INTO user_notification (user_id, property_id) VALUES ";
            final Collection<String> values = matchingProperties.stream().
                    map(property -> String.format("(%d, %d)", renter.getId(), property.getID()))
                    .collect(Collectors.toList());
            query += String.join(", ", values);

            try {
                final PreparedStatement ps = connection.prepareStatement(query);
                final int rowsUpdated = ps.executeUpdate();

                System.out.println("DEBUG: " + rowsUpdated + " row(s) updated from fetching new notifications");
            } catch (final SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public void logout() {
        currentUser = null;
    }

    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }

    public boolean isUserLoggedIn() {
        return currentUser != null;
    }

    public void logOutUser() {
        currentUser = null;
    }

    public static LoginController getOnlyInstance() {
        return loginController;
    }
}
