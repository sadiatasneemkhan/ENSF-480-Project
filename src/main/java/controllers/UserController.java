package controllers;

import models.Renter;
import models.User;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class UserController {
    private static final UserController userController = new UserController();
    private final Connection connection = DatabaseConnection.getConnection();


    private UserController() {}

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
