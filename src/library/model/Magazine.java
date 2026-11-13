package library.model;

public class Magazine extends LibraryItem {
    public Magazine(String id, String title, String publisher, int year) {
        super(id, title, publisher, year);
    }

    @Override
    public ItemType getType() {
        return ItemType.MAGAZINE;
    }

    @Override
    public int getBaseLoanDays() {
        return 7;
    }

    @Override
    public double getDailyFineRate() {
        return 0.30;
    }

    @Override
    public String getDetails() {
        return "Magazine '" + getTitle() + "' by " + getCreator() + " (" + getYear() + ")";
    }
}
