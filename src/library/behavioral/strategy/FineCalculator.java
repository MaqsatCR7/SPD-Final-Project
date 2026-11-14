package library.behavioral.strategy;

/** Context of the STRATEGY pattern: delegates the calculation to the current strategy. */
public class FineCalculator {
    private FineStrategy strategy;

    public FineCalculator(FineStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(FineStrategy strategy) {
        this.strategy = strategy;
    }

    public FineStrategy getStrategy() {
        return strategy;
    }

    /** Returns the fine rounded to 2 decimals. */
    public double calculate(long overdueDays, double dailyRate) {
        if (overdueDays <= 0) {
            return 0.0;
        }
        return Math.round(strategy.calculate(overdueDays, dailyRate) * 100.0) / 100.0;
    }
}
