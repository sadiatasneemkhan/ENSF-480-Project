package models;

public class Manager extends User {
    public Manager(final String username, final String password, final Boolean isRegistered) {
        super(username, password, isRegistered);
        this.role = UserRole.MANAGER;
    }
}