package library.model;

import library.structural.decorator.LoanPolicy;
import library.util.IdGenerator;

import java.time.LocalDate;

public class Loan {
    private final String id = IdGenerator.next("LN");
    private final LibraryItem item;
    private final Member member;
    private final LocalDate loanDate;
    private final LocalDate dueDate;
    private final String policyDescription;
    private final double extraFee;
    private LocalDate returnDate;
    private double fine;

    public Loan(LibraryItem item, Member member, LocalDate loanDate, LoanPolicy policy) {
        this.item = item;
        this.member = member;
        this.loanDate = loanDate;
        this.dueDate = loanDate.plusDays(policy.getLoanDays());
        this.policyDescription = policy.getDescription();
        this.extraFee = policy.getFee();
    }

    public String getId() {
        return id;
    }

    public LibraryItem getItem() {
        return item;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getPolicyDescription() {
        return policyDescription;
    }

    public double getExtraFee() {
        return extraFee;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    public boolean isActive() {
        return returnDate == null;
    }

    @Override
    public String toString() {
        String status = isActive()
                ? "ACTIVE, due " + dueDate
                : "RETURNED " + returnDate + ", fine " + String.format("%.2f", fine);
        return String.format("%s | %s -> %s | %s | %s", id, member.getName(), item.getTitle(),
                policyDescription, status);
    }
}
