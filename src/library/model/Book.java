package library.model;

public class Book extends LibraryItem {
    public Book(String id, String title, String author, int year) {
        super(id, title, author, year);
    }

    @Override
    public ItemType getType() {
        return ItemType.BOOK;
    }

    @Override
    public int getBaseLoanDays() {
        return 14;
    }

    @Override
    public double getDailyFineRate() {
        return 0.50;
    }

    @Override
    public String getDetails() {
        return "Book '" + getTitle() + "' by " + getCreator() + " (" + getYear() + ")";
    }
}
