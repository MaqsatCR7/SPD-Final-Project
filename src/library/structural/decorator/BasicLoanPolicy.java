package library.structural.decorator;

import library.model.LibraryItem;

/** Concrete component: the standard, free loan for an item. */
public class BasicLoanPolicy implements LoanPolicy {
    private final LibraryItem item;

    public BasicLoanPolicy(LibraryItem item) {
        this.item = item;
    }

    @Override
    public int getLoanDays() {
        return item.getBaseLoanDays();
    }

    @Override
    public double getFee() {
        return 0.0;
    }

    @Override
    public String getDescription() {
        return "Standard loan (" + item.getBaseLoanDays() + "d)";
    }
}
