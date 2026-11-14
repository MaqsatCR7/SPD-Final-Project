package library.behavioral.strategy;

/** Strategy interface of the STRATEGY pattern: how a late-return fine is calculated. */
public interface FineStrategy {
    double calculate(long overdueDays, double dailyRate);

    String getName();
}
