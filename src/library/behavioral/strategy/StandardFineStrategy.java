package library.behavioral.strategy;

/** Full price, capped at 30.00. */
public class StandardFineStrategy implements FineStrategy {
    private static final double MAX_FINE = 30.0;

    @Override
    public double calculate(long overdueDays, double dailyRate) {
        return Math.min(MAX_FINE, overdueDays * dailyRate);
    }

    @Override
    public String getName() {
        return "Standard";
    }
}
