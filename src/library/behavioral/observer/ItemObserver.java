package library.behavioral.observer;

/** Observer interface of the OBSERVER pattern. */
public interface ItemObserver {
    void update(ItemEvent event);
}
