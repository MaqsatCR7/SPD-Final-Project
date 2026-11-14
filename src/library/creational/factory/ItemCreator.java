package library.creational.factory;

import library.model.LibraryItem;

/** Creator interface of the FACTORY METHOD pattern. */
public interface ItemCreator {
    LibraryItem create(String id, String title, String creator, int year);
}
