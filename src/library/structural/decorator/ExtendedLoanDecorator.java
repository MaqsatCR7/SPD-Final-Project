package library.structural.decorator;

/** Adds 7 extra days for a small fee. */
public class ExtendedLoanDecorator extends LoanPolicyDecorator {
    public ExtendedLoanDecorator(LoanPolicy wrapped) {
        super(wrapped);
    }

    @Override
    public int getLoanDays() {
        return super.getLoanDays() + 7;
    }

    @Override
    public double getFee() {
        return super.getFee() + 1.00;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + Extended(+7d)";
    }
}
