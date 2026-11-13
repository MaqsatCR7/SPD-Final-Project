package library.creational.factory;

import library.model.Book;
import library.model.LibraryItem;

public class BookCreator implements ItemCreator {
    @Override
    public LibraryItem create(String id, String title, String author, int year) {
        return new Book(id, title, author, year);
    }
}
