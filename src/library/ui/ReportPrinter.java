package library.ui;

import library.model.LibraryItem;
import library.model.Loan;
import library.model.Member;

import java.util.Collection;
import java.util.List;

/** Console output only (Single Responsibility: no business logic here). */
public final class ReportPrinter {
    private ReportPrinter() {
    }

    public static void printItems(Collection<LibraryItem> items) {
        System.out.println("--- ITEMS (" + items.size() + ") ---");
        if (items.isEmpty()) {
            System.out.println("   (none)");
        }
        for (LibraryItem item : items) {
            System.out.println("   " + item);
        }
    }

    public static void printMembers(Collection<Member> members) {
        System.out.println("--- MEMBERS (" + members.size() + ") ---");
        if (members.isEmpty()) {
            System.out.println("   (none)");
        }
        for (Member member : members) {
            System.out.println("   " + member);
        }
    }

    public static void printLoans(List<Loan> loans) {
        System.out.println("--- LOANS (" + loans.size() + ") ---");
        if (loans.isEmpty()) {
            System.out.println("   (none)");
        }
        for (Loan loan : loans) {
            System.out.println("   " + loan);
        }
    }

    public static void printHistory(List<String> log) {
        System.out.println("--- COMMAND HISTORY ---");
        if (log.isEmpty()) {
            System.out.println("   (empty)");
        }
        for (String line : log) {
            System.out.println("   " + line);
        }
    }
}
