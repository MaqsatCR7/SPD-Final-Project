package library.behavioral.strategy;

/** 50% discount for students, capped at 15.00. */
public class StudentFineStrategy implements FineStrategy {
    private static final double DISCOUNT = 0.5;
    private static final double MAX_FINE = 15.0;

    @Override
    public double calculate(long overdueDays, double dailyRate) {
        return Math.min(MAX_FINE, overdueDays * dailyRate * DISCOUNT);
    }

    @Override
    public String getName() {
        return "Student (-50%)";
    }
}
