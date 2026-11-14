package library.model;

public class Dvd extends LibraryItem {
    public Dvd(String id, String title, String director, int year) {
        super(id, title, director, year);
    }

    @Override
    public ItemType getType() {
        return ItemType.DVD;
    }

    @Override
    public int getBaseLoanDays() {
        return 5;
    }

    @Override
    public double getDailyFineRate() {
        return 1.00;
    }

    @Override
    public String getDetails() {
        return "DVD '" + getTitle() + "' directed by " + getCreator() + " (" + getYear() + ")";
    }
}
