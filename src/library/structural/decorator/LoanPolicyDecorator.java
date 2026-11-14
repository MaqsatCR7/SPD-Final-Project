package library.structural.decorator;

/** Base decorator: wraps another policy and delegates by default. */
public abstract class LoanPolicyDecorator implements LoanPolicy {
    protected final LoanPolicy wrapped;

    protected LoanPolicyDecorator(LoanPolicy wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public int getLoanDays() {
        return wrapped.getLoanDays();
    }

    @Override
    public double getFee() {
        return wrapped.getFee();
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription();
    }
}
