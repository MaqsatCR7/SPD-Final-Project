package library.model;

public enum MembershipType {
    STANDARD(3), STUDENT(5), PREMIUM(10);

    private final int maxLoans;

    MembershipType(int maxLoans) {
        this.maxLoans = maxLoans;
    }

    public int getMaxLoans() {
        return maxLoans;
    }
}
