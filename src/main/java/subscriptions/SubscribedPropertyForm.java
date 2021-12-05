package subscriptions;

import models.DatabaseModel;
import models.Property;
import models.PropertyForm;

import java.util.Collection;

public class SubscribedPropertyForm extends DatabaseModel implements Subject {
    private static final String tableName = "subscribed_property_form";

    private PropertyForm propertyForm;
    private Collection<Observer<Property>> subscribedRenters;

    private Collection<Property> newProperties;

    @Override
    public void registerObserver(final Observer observer) {
        subscribedRenters.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        subscribedRenters.remove(observer);
    }

    @Override
    public void notifyObservers() {
    }
}
