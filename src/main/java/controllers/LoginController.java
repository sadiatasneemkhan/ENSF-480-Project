package controllers;

import models.User;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

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

    private boolean checkUserExists(final String email) throws SQLException {
        final String query = "SELECT EXISTS(SELECT * FROM user WHERE email = ?)";
        final PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, email);
        final ResultSet rs = ps.executeQuery();

        rs.next();
        return rs.getBoolean(1);
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
