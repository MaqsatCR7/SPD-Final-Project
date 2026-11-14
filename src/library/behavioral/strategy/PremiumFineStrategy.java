package library.behavioral.strategy;

/** Premium members get 3 days of grace and a low cap of 10.00. */
public class PremiumFineStrategy implements FineStrategy {
    private static final int GRACE_DAYS = 3;
    private static final double MAX_FINE = 10.0;

    @Override
    public double calculate(long overdueDays, double dailyRate) {
        long chargeable = Math.max(0, overdueDays - GRACE_DAYS);
        return Math.min(MAX_FINE, chargeable * dailyRate);
    }

    @Override
    public String getName() {
        return "Premium (3 grace days)";
    }
}
