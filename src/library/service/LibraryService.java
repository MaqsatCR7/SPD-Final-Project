package library.service;

import library.behavioral.strategy.FineCalculator;
import library.behavioral.strategy.FineStrategyProvider;
import library.creational.singleton.LibraryDatabase;
import library.exception.LibraryException;
import library.model.LibraryItem;
import library.model.Loan;
import library.model.Member;
import library.structural.decorator.LoanPolicy;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Business logic (Single Responsibility: only the library rules live here).
 * It is the "Receiver" used by the Command objects.
 */
public class LibraryService {
    private final LibraryDatabase db;

    public LibraryService(LibraryDatabase db) {
        this.db = db;
    }

    public Loan checkout(Member member, LibraryItem item, LoanPolicy policy, LocalDate date) {
        if (!item.isAvailable()) {
            throw new LibraryException("'" + item.getTitle() + "' is already borrowed");
        }
        if (db.countActiveLoans(member) >= member.getMembershipType().getMaxLoans()) {
            throw new LibraryException(member.getName() + " reached the limit of "
                    + member.getMembershipType().getMaxLoans() + " loans");
        }
        Loan loan = new Loan(item, member, date, policy);
        db.addLoan(loan);
        item.setAvailable(false);
        return loan;
    }

    public void cancelCheckout(Loan loan) {
        db.removeLoan(loan);
        loan.getItem().setAvailable(true);
    }

    /** Returns the item and gives back the calculated fine. */
    public double returnItem(Loan loan, LocalDate returnDate) {
        if (!loan.isActive()) {
            throw new LibraryException("Loan " + loan.getId() + " is already closed");
        }
        long overdueDays = Math.max(0, ChronoUnit.DAYS.between(loan.getDueDate(), returnDate));
        FineCalculator calculator = new FineCalculator(
                FineStrategyProvider.forMembership(loan.getMember().getMembershipType()));
        double fine = calculator.calculate(overdueDays, loan.getItem().getDailyFineRate());
        loan.setReturnDate(returnDate);
        loan.setFine(fine);
        loan.getItem().setAvailable(true); // triggers Observer notifications
        return fine;
    }

    public void cancelReturn(Loan loan) {
        loan.setReturnDate(null);
        loan.setFine(0.0);
        loan.getItem().setAvailable(false);
    }

    public void reserve(Member member, LibraryItem item) {
        if (item.isAvailable()) {
            throw new LibraryException("'" + item.getTitle() + "' is available - borrow it instead");
        }
        item.subscribe(member);
    }

    public void cancelReservation(Member member, LibraryItem item) {
        item.unsubscribe(member);
    }
}
