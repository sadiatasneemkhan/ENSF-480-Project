package models;

public class Landlord extends User {
    protected Landlord(final String email, final String password) {
        super(email, password);
        this.role = UserRole.LANDLORD;
    }
}