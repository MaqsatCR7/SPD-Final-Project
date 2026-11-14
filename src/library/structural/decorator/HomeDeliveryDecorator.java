package library.structural.decorator;

/** Adds home delivery of the item. */
public class HomeDeliveryDecorator extends LoanPolicyDecorator {
    public HomeDeliveryDecorator(LoanPolicy wrapped) {
        super(wrapped);
    }

    @Override
    public double getFee() {
        return super.getFee() + 3.00;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + Home delivery";
    }
}
