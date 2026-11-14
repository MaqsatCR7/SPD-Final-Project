package library.ui;

import library.creational.singleton.LibraryDatabase;
import library.exception.LibraryException;
import library.model.ItemType;
import library.model.LibraryItem;
import library.model.Loan;
import library.model.Member;
import library.model.MembershipType;
import library.structural.adapter.LegacyCatalogAdapter;
import library.structural.adapter.LegacyCatalogSystem;
import library.structural.decorator.LoanExtra;
import library.structural.facade.LibraryFacade;

import java.time.LocalDate;

/** Scripted scenario that shows every pattern working together. */
public class Demo {
    private final LibraryFacade library;

    public Demo(LibraryFacade library) {
        this.library = library;
    }

    public void run() {
        LocalDate today = LocalDate.now();

        title("1. SINGLETON - one shared database");
        System.out.println("   Same instance? "
                + (LibraryDatabase.getInstance() == LibraryDatabase.getInstance()));

        title("2. BUILDER - registering members");
        Member aruzhan = library.registerMember("Aruzhan", "aruzhan@aitu.edu.kz", "+7 700 111 22 33", MembershipType.STUDENT);
        Member nurlan = library.registerMember("Nurlan", "nurlan@mail.kz", "+7 701 222 33 44", MembershipType.STANDARD);
        Member dana = library.registerMember("Dana", "dana@mail.kz", "+7 702 333 44 55", MembershipType.PREMIUM);
        ReportPrinter.printMembers(library.getMembers());

        title("3. FACTORY METHOD - creating items by type");
        LibraryItem java = library.addItem(ItemType.BOOK, "Effective Java", "Joshua Bloch", 2018);
        LibraryItem dune = library.addItem(ItemType.BOOK, "Dune", "Frank Herbert", 1965);
        LibraryItem forbes = library.addItem(ItemType.MAGAZINE, "Forbes", "Forbes Media", 2025);
        LibraryItem matrix = library.addItem(ItemType.DVD, "The Matrix", "Wachowskis", 1999);
        ReportPrinter.printItems(library.getItems());

        title("4. ADAPTER - importing a legacy catalog");
        int imported = library.importCatalog(new LegacyCatalogAdapter(new LegacyCatalogSystem()));
        System.out.println("   Imported items: " + imported);

        title("5. DECORATOR + COMMAND - checkout with extras");
        Loan loan1 = library.checkout(aruzhan.getId(), java.getId(), today,
                LoanExtra.EXTENDED, LoanExtra.SMS_REMINDER);
        System.out.println("   " + loan1);
        System.out.printf("   Extra fee for options: %.2f%n", loan1.getExtraFee());
        Loan loan2 = library.checkout(nurlan.getId(), matrix.getId(), today, LoanExtra.HOME_DELIVERY);
        System.out.println("   " + loan2);
        Loan loan3 = library.checkout(dana.getId(), dune.getId(), today);
        System.out.println("   " + loan3);

        title("6. Business rule - cannot borrow a borrowed item");
        try {
            library.checkout(nurlan.getId(), java.getId(), today);
        } catch (LibraryException e) {
            System.out.println("   Error (expected): " + e.getMessage());
        }

        title("7. OBSERVER - reserve a borrowed item, get notified on return");
        library.reserve(nurlan.getId(), java.getId());
        System.out.println("   Nurlan reserved 'Effective Java'. Aruzhan returns it 9 days after the due date:");
        double fine1 = library.returnItem(java.getId(), today.plusDays(30));
        System.out.printf("   Fine (STUDENT strategy): %.2f%n", fine1);

        title("8. STRATEGY - same delay, different fines");
        double fine2 = library.returnItem(matrix.getId(), today.plusDays(10));
        System.out.printf("   Nurlan (STANDARD) returns DVD 5 days late: %.2f%n", fine2);
        double fine3 = library.returnItem(dune.getId(), today.plusDays(20));
        System.out.printf("   Dana (PREMIUM, 3 grace days) returns book 6 days late: %.2f%n", fine3);

        title("9. COMMAND - undo the last action");
        System.out.println("   Undo: " + library.undoLastAction());
        System.out.println("   'Dune' is borrowed again after undoing the return? " + !dune.isAvailable());

        title("10. FACADE - final reports (one simple entry point)");
        ReportPrinter.printLoans(library.getLoans());
        ReportPrinter.printHistory(library.getHistoryLog());
        System.out.println("   Search 'java': " + library.search("java").size() + " result(s)");
        System.out.println("   Nurlan's notifications: " + nurlan.getNotifications());
    }

    private void title(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }
}
