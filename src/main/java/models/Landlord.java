package models;

public class Landlord extends User {
    protected Landlord(final String username, final String password) {
        super(username, password);
        this.role = UserRole.LANDLORD;
    }
}