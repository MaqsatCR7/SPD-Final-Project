package library.behavioral.observer;

import library.model.LibraryItem;

/** Event sent from a library item (subject) to its observers. */
public record ItemEvent(LibraryItem item, Type type) {
    public enum Type {
        AVAILABLE, BORROWED
    }
}
