package subscriptions;

public interface Subject {
    void registerObserver(final Observer observer);
    void unregisterObserver(final Observer observer);
    void notifyObservers();
}
