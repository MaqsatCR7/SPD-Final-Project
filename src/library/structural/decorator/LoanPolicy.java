package library.structural.decorator;

/** Component interface of the DECORATOR pattern. */
public interface LoanPolicy {
    int getLoanDays();

    double getFee();

    String getDescription();
}
