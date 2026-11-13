package library.creational.factory;

import library.model.Dvd;
import library.model.LibraryItem;

public class DvdCreator implements ItemCreator {
    @Override
    public LibraryItem create(String id, String title, String director, int year) {
        return new Dvd(id, title, director, year);
    }
}
