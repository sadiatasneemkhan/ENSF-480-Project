package models;

public class Landlord extends User {
    protected Landlord(final String username, final String password, final Boolean isRegistered) {
        super(username, password, isRegistered);
        this.role = UserRole.LANDLORD;
    }
}