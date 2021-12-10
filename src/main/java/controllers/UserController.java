package controllers;

import models.User;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class UserController {
    private static final UserController userController = new UserController();

    private final Connection connection = DatabaseConnection.getConnection();


    private UserController() {}

    public Optional<User> updateUserPropertyForm(final User user) {
        if (user.getPropertyFormId() == null) {
            throw new IllegalArgumentException("No property form ID on user");
        }
        final String query = "UPDATE user SET property_form_id = ? WHERE id = ? LIMIT 1";
        try {
            final PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, user.getPropertyFormId());
            ps.setInt(2, user.getId());

            final int updatedRows = ps.executeUpdate();
            if (updatedRows < 1) {
                System.err.println("No rows updated from updating user's property form");
            } else {
                return Optional.of(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Collection<? extends User> getUsersOfRole(final User.UserRole role) {
        final String query = "SELECT * FROM user WHERE role = ?";
        final Collection<User> users = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, role.toString());
            final ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User.createFromResultSet(rs).ifPresent(users::add);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }

    public static UserController getOnlyInstance() {
        return userController;
    }
}
