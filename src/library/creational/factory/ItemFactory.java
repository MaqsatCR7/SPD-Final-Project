package library.creational.factory;

import library.exception.LibraryException;
import library.model.ItemType;
import library.model.LibraryItem;

import java.util.EnumMap;
import java.util.Map;

/**
 * FACTORY METHOD pattern: clients ask for an {@link ItemType} and never use "new Book(...)".
 * New item types are added by registering a new {@link ItemCreator}
 * (Open/Closed Principle) - no existing code has to be modified.
 */
public final class ItemFactory {
    private static final Map<ItemType, ItemCreator> CREATORS = new EnumMap<>(ItemType.class);

    static {
        register(ItemType.BOOK, new BookCreator());
        register(ItemType.MAGAZINE, new MagazineCreator());
        register(ItemType.DVD, new DvdCreator());
    }

    private ItemFactory() {
    }

    public static void register(ItemType type, ItemCreator creator) {
        CREATORS.put(type, creator);
    }

    public static LibraryItem create(ItemType type, String id, String title, String creator, int year) {
        ItemCreator itemCreator = CREATORS.get(type);
        if (itemCreator == null) {
            throw new LibraryException("No creator registered for type " + type);
        }
        return itemCreator.create(id, title, creator, year);
    }
}
