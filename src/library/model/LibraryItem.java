package library.model;

import library.behavioral.observer.ItemEvent;
import library.behavioral.observer.ItemObserver;
import library.behavioral.observer.ItemSubject;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Abstract product of the Factory Method pattern and the Subject of the Observer pattern.
 * Subclasses only describe what differs (loan days, fine rate, details) - Open/Closed Principle.
 */
public abstract class LibraryItem implements ItemSubject {
    private final String id;
    private final String title;
    private final String creator;
    private final int year;
    private boolean available = true;
    // CopyOnWrite: an observer may unsubscribe itself while being notified.
    private final List<ItemObserver> observers = new CopyOnWriteArrayList<>();

    protected LibraryItem(String id, String title, String creator, int year) {
        this.id = id;
        this.title = title;
        this.creator = creator;
        this.year = year;
    }

    public abstract ItemType getType();

    public abstract int getBaseLoanDays();

    public abstract double getDailyFineRate();

    public abstract String getDetails();

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCreator() {
        return creator;
    }

    public int getYear() {
        return year;
    }

    public boolean isAvailable() {
        return available;
    }

    /** Changes availability and notifies observers about the change. */
    public void setAvailable(boolean available) {
        if (this.available == available) {
            return;
        }
        this.available = available;
        notifyObservers(new ItemEvent(this,
                available ? ItemEvent.Type.AVAILABLE : ItemEvent.Type.BORROWED));
    }

    @Override
    public void subscribe(ItemObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void unsubscribe(ItemObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(ItemEvent event) {
        for (ItemObserver observer : observers) {
            observer.update(event);
        }
    }

    @Override
    public String toString() {
        return id + " " + getDetails() + (available ? " [available]" : " [borrowed]");
    }
}
