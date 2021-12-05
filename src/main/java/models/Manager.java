package models;

public class Manager extends User {
    public Manager(final String username, final String password) {
        super(username, password);
        this.role = UserRole.MANAGER;
    }
}