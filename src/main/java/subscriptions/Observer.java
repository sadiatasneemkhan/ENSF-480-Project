package subscriptions;

import java.util.Collection;

public interface Observer<T> {
    void update(Collection<T> updates);
}
