package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

abstract public class User extends DatabaseModel {
    private static final String tableName = "user";

    String username;
    String password;
    UserRole role;
    Boolean isRegistered;

    protected User(final String username, final String password, final Boolean isRegistered) {
        this.username = username;
        this.password = password;
        this.isRegistered = isRegistered;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(final UserRole role) {
        this.role = role;
    }

    public Boolean getRegistered() {
        return isRegistered;
    }

    public void setRegistered(final Boolean registered) {
        isRegistered = registered;
    }

    public enum UserRole {
        RENTER,
        LANDLORD,
        MANAGER;

        public static final UserRole[] fromId = UserRole.values();
    }

    public static Optional<User> createFromResultSet(final ResultSet rs) {
        User user = null;
        try {
            if (rs.isBeforeFirst()) {
                rs.next();
            }
            final Integer id = rs.getInt("id");
            final String username = rs.getString("username");
            final String password = rs.getString("password");
            final Boolean isRegistered = rs.getBoolean("is_registered");
            final UserRole role = UserRole.valueOf(rs.getString("role"));

            user = fromRole(role, username, password, isRegistered);
            user.setId(id);
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(user);
    }

    public static User fromRole(final UserRole role, final String username, final String password, final boolean isRegistered) {
        User user = null;

        switch (role) {
            case RENTER:
                user = new Renter(username, password, isRegistered);
                break;
            case LANDLORD:
                user = new Landlord(username, password, isRegistered);
                break;
            case MANAGER:
                user = new Manager(username, password, isRegistered);
                break;
            default:
                System.err.println("Mismatching role?");
                break;
        }

        return user;
    }
}
