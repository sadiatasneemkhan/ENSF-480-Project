package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

abstract public class User extends DatabaseModel {
    private static final String tableName = "user";

    protected String email;
    protected String password;
    protected UserRole role;
    protected LocalDateTime lastLogin;
    protected Integer propertyFormId;

    protected User(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public static Optional<User> createFromResultSet(final ResultSet rs) {
        User user = null;
        try {
            if (rs.isBeforeFirst()) {
                rs.next();
            }
            final Integer id = rs.getInt("id");
            final String email = rs.getString("email");
            final String password = rs.getString("password");
            final UserRole role = UserRole.valueOf(rs.getString("role"));
            final LocalDateTime lastLogin = rs.getObject("last_login", LocalDateTime.class);
            final Integer propertyFormId = rs.getInt("property_form_id");

            user = fromRole(role, email, password);
            user.setId(id);
            user.setLastLogin(lastLogin);
            user.setPropertyFormId(propertyFormId);
        } catch (final SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(user);
    }

    public static User fromRole(final UserRole role, final String email, final String password) {
        User user = null;

        switch (role) {
            case RENTER:
                user = new Renter(email, password);
                break;
            case LANDLORD:
                user = new Landlord(email, password);
                break;
            case MANAGER:
                user = new Manager(email, password);
                break;
            default:
                System.err.println("Mismatching role?");
                break;
        }

        return user;
    }

    public Integer getPropertyFormId() {
        return propertyFormId;
    }

    public void setPropertyFormId(final Integer propertyFormId) {
        this.propertyFormId = propertyFormId;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(final LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
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

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", role=" + role +
                '}';
    }

    public enum UserRole {
        RENTER,
        LANDLORD,
        MANAGER;
    }
}
