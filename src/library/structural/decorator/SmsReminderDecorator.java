package library.structural.decorator;

/** Adds an SMS reminder before the due date. */
public class SmsReminderDecorator extends LoanPolicyDecorator {
    public SmsReminderDecorator(LoanPolicy wrapped) {
        super(wrapped);
    }

    @Override
    public double getFee() {
        return super.getFee() + 0.50;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + SMS reminder";
    }
}
