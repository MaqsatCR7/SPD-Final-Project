package library.behavioral.observer;

/** Subject interface of the OBSERVER pattern. */
public interface ItemSubject {
    void subscribe(ItemObserver observer);

    void unsubscribe(ItemObserver observer);

    void notifyObservers(ItemEvent event);
}
