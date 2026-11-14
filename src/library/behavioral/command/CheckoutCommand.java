package library.behavioral.command;

import library.model.LibraryItem;
import library.model.Loan;
import library.model.Member;
import library.service.LibraryService;
import library.structural.decorator.LoanPolicy;

import java.time.LocalDate;

public class CheckoutCommand implements Command {
    private final LibraryService service;
    private final Member member;
    private final LibraryItem item;
    private final LoanPolicy policy;
    private final LocalDate date;
    private Loan loan;

    public CheckoutCommand(LibraryService service, Member member, LibraryItem item,
                           LoanPolicy policy, LocalDate date) {
        this.service = service;
        this.member = member;
        this.item = item;
        this.policy = policy;
        this.date = date;
    }

    @Override
    public void execute() {
        loan = service.checkout(member, item, policy, date);
    }

    @Override
    public void undo() {
        if (loan != null) {
            service.cancelCheckout(loan);
        }
    }

    @Override
    public String describe() {
        return "Checkout '" + item.getTitle() + "' for " + member.getName();
    }

    public Loan getLoan() {
        return loan;
    }
}
