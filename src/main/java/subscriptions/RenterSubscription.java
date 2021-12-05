package subscriptions;

import models.DatabaseModel;
import models.Property;
import models.Renter;

import java.util.Collection;


public class RenterSubscription extends DatabaseModel implements Observer<Property> {
    private Renter subscribedRenter;
    private Collection<Property> updates;

    public RenterSubscription(final Renter subscribedRenter) {
        this.subscribedRenter = subscribedRenter;
    }

    public Renter getSubscribedRenter() {
        return subscribedRenter;
    }

    public void setSubscribedRenter(final Renter subscribedRenter) {
        this.subscribedRenter = subscribedRenter;
    }

    @Override
    public void update(final Collection<Property> updates) {
        // send notifications
    }
}
