package models;

public class Renter extends User {

    protected Renter(final String username, final String password, final Boolean isRegistered) {
        super(username, password, isRegistered);
        this.role = UserRole.RENTER;
    }
}
