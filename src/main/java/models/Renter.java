package models;

import java.util.Objects;

public class Renter extends User {

    protected Renter(final String username, final String password) {
        super(username, password);
        this.role = UserRole.RENTER;
    }

    @Override
    public boolean equals(final Object o) {
        return Objects.equals(getId(), ((Renter) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
