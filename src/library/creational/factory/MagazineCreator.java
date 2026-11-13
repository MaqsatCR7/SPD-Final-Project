package library.creational.factory;

import library.model.LibraryItem;
import library.model.Magazine;

public class MagazineCreator implements ItemCreator {
    @Override
    public LibraryItem create(String id, String title, String publisher, int year) {
        return new Magazine(id, title, publisher, year);
    }
}
