package controllers;

import models.User;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class LoginController {
    private static final LoginController loginController = new LoginController();
    private final Connection connection = DatabaseConnection.getConnection();

    private Optional<User> currentUser = Optional.empty();

    private LoginController() { }

    public Optional<User> login(final String username, final String password) {
        try {
            final String query = "SELECT * FROM user WHERE username = ? AND password = ? LIMIT 1";
            final PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            final ResultSet rs = ps.executeQuery();
            final Optional<User> user = User.createFromResultSet(rs);
            if (user.isPresent()) {
                System.out.println("Successfully logged in " + user.get().getUsername());
                currentUser = user;
            } else {
                throw new RuntimeException("Error logging " + username + " in.");
            }
        } catch (final SQLException | RuntimeException throwables) {
            throwables.printStackTrace();
        }

        return currentUser;
    }

    public Optional<User> register(final String username, final String password, final User.UserRole role) throws IllegalArgumentException {
        try {
            if (checkUserExists(username)) {
                throw new IllegalArgumentException("Username " + username + " is taken");
            }
        } catch (final SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        final User user = User.fromRole(role, username, password, true);
        final String query = "INSERT INTO user(username, password, role, is_registered) VALUES (?,?,?,?)";
        try {
            final PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().toString());
            ps.setBoolean(4, user.getRegistered());

            final int row = ps.executeUpdate();
            System.out.println(row + " row(s) affected from register.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return login(username, password);
    }

    private boolean checkUserExists(final String username) throws SQLException {
        final String query = "SELECT EXISTS(SELECT * FROM user WHERE username = ?)";
        final PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, username);
        final ResultSet rs = ps.executeQuery();

        rs.next();
        return rs.getBoolean(1);
    }

    public void logout() {
        currentUser = Optional.empty();
    }

    public Optional<User> getCurrentUser() {
        return currentUser;
    }

    public boolean isUserLoggedIn() {
        return currentUser.isPresent();
    }

    public void logOutUser() {
        currentUser = Optional.empty();
    }

    public static LoginController getOnlyInstance() {
        return loginController;
    }
}
