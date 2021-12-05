package models;

public class Manager extends User {
    public Manager(final String email, final String password) {
        super(email, password);
        this.role = UserRole.MANAGER;
    }
}