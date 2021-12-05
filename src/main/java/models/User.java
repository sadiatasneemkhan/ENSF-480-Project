package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

abstract public class User extends DatabaseModel {
    private static final String tableName = "user";

    protected String username;
    protected String password;
    protected UserRole role;
    protected LocalDateTime lastLogin;

    protected User(final String username, final String password) {
        this.username = username;
        this.password = password;
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
            final UserRole role = UserRole.valueOf(rs.getString("role"));
            final LocalDateTime lastLogin = rs.getObject("last_login", LocalDateTime.class);

            user = fromRole(role, username, password);
            user.setId(id);
            user.setLastLogin(lastLogin);
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(user);
    }

    public static User fromRole(final UserRole role, final String username, final String password) {
        User user = null;

        switch (role) {
            case RENTER:
                user = new Renter(username, password);
                break;
            case LANDLORD:
                user = new Landlord(username, password);
                break;
            case MANAGER:
                user = new Manager(username, password);
                break;
            default:
                System.err.println("Mismatching role?");
                break;
        }

        return user;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(final LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", role=" + role +
                '}';
    }

    public void setRole(final UserRole role) {
        this.role = role;
    }

    public enum UserRole {
        RENTER,
        LANDLORD,
        MANAGER;
    }
}
