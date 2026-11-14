package library.behavioral.command;

import library.model.Loan;
import library.service.LibraryService;

import java.time.LocalDate;

public class ReturnCommand implements Command {
    private final LibraryService service;
    private final Loan loan;
    private final LocalDate date;
    private double fine;

    public ReturnCommand(LibraryService service, Loan loan, LocalDate date) {
        this.service = service;
        this.loan = loan;
        this.date = date;
    }

    @Override
    public void execute() {
        fine = service.returnItem(loan, date);
    }

    @Override
    public void undo() {
        service.cancelReturn(loan);
    }

    @Override
    public String describe() {
        return "Return '" + loan.getItem().getTitle() + "' by " + loan.getMember().getName();
    }

    public double getFine() {
        return fine;
    }
}
