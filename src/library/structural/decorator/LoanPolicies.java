package library.structural.decorator;

import library.model.LibraryItem;

/** Helper that wraps a basic policy with the requested decorators (in any combination). */
public final class LoanPolicies {
    private LoanPolicies() {
    }

    public static LoanPolicy build(LibraryItem item, LoanExtra... extras) {
        LoanPolicy policy = new BasicLoanPolicy(item);
        for (LoanExtra extra : extras) {
            policy = switch (extra) {
                case EXTENDED -> new ExtendedLoanDecorator(policy);
                case SMS_REMINDER -> new SmsReminderDecorator(policy);
                case HOME_DELIVERY -> new HomeDeliveryDecorator(policy);
            };
        }
        return policy;
    }
}
