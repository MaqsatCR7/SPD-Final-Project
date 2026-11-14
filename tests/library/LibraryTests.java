package library;

import library.behavioral.strategy.FineCalculator;
import library.behavioral.strategy.PremiumFineStrategy;
import library.behavioral.strategy.StandardFineStrategy;
import library.behavioral.strategy.StudentFineStrategy;
import library.creational.factory.ItemFactory;
import library.creational.singleton.LibraryDatabase;
import library.exception.LibraryException;
import library.model.Book;
import library.model.Dvd;
import library.model.ItemType;
import library.model.LibraryItem;
import library.model.Loan;
import library.model.Magazine;
import library.model.Member;
import library.model.MembershipType;
import library.structural.adapter.LegacyCatalogAdapter;
import library.structural.adapter.LegacyCatalogSystem;
import library.structural.decorator.LoanExtra;
import library.structural.decorator.LoanPolicies;
import library.structural.decorator.LoanPolicy;
import library.structural.facade.LibraryFacade;

import java.time.LocalDate;

/** Tiny dependency-free test runner:  java -cp out library.LibraryTests */
public class LibraryTests {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        singleton();
        factory();
        builder();
        adapter();
        decorator();
        strategy();
        observer();
        commandUndo();
        facadeRules();
        System.out.println();
        System.out.println("Passed: " + passed + ", Failed: " + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void singleton() {
        check(LibraryDatabase.getInstance() == LibraryDatabase.getInstance(), "Singleton returns one instance");
    }

    private static void factory() {
        check(ItemFactory.create(ItemType.BOOK, "T-1", "a", "b", 2000) instanceof Book, "Factory creates Book");
        check(ItemFactory.create(ItemType.MAGAZINE, "T-2", "a", "b", 2000) instanceof Magazine, "Factory creates Magazine");
        check(ItemFactory.create(ItemType.DVD, "T-3", "a", "b", 2000) instanceof Dvd, "Factory creates Dvd");
    }

    private static void builder() {
        Member m = new Member.Builder("Ali", "ali@mail.kz").membershipType(MembershipType.STUDENT).build();
        check(m.getMembershipType() == MembershipType.STUDENT && "-".equals(m.getPhone()), "Builder sets optional fields and defaults");
        check(throwsLibraryException(() -> new Member.Builder("Ali", "bad-email").build()), "Builder rejects invalid e-mail");
        check(throwsLibraryException(() -> new Member.Builder(" ", "a@b.kz").build()), "Builder rejects empty name");
    }

    private static void adapter() {
        var items = new LegacyCatalogAdapter(new LegacyCatalogSystem()).fetchItems();
        check(items.size() == 4, "Adapter converts 4 valid records and skips the broken one");
    }

    private static void decorator() {
        LibraryItem book = ItemFactory.create(ItemType.BOOK, "T-4", "a", "b", 2000);
        LoanPolicy plain = LoanPolicies.build(book);
        check(plain.getLoanDays() == 14 && plain.getFee() == 0.0, "Basic loan: 14 days, free");
        LoanPolicy full = LoanPolicies.build(book, LoanExtra.EXTENDED, LoanExtra.SMS_REMINDER);
        check(full.getLoanDays() == 21 && Math.abs(full.getFee() - 1.5) < 1e-9, "Decorators stack: 21 days, fee 1.50");
    }

    private static void strategy() {
        check(new FineCalculator(new StandardFineStrategy()).calculate(10, 0.5) == 5.0, "Standard fine 10d x 0.50 = 5.00");
        check(new FineCalculator(new StudentFineStrategy()).calculate(10, 0.5) == 2.5, "Student fine is 50% cheaper");
        check(new FineCalculator(new PremiumFineStrategy()).calculate(10, 0.5) == 3.5, "Premium fine has 3 grace days");
        check(new FineCalculator(new StandardFineStrategy()).calculate(0, 0.5) == 0.0, "No fine when not overdue");
        FineCalculator calc = new FineCalculator(new StandardFineStrategy());
        calc.setStrategy(new StudentFineStrategy());
        check(calc.calculate(10, 0.5) == 2.5, "Strategy can be swapped at runtime");
    }

    private static LibraryFacade freshLibrary() {
        LibraryDatabase.getInstance().clear();
        return new LibraryFacade();
    }

    private static void observer() {
        LibraryFacade lib = freshLibrary();
        Member a = lib.registerMember("A", "a@a.kz", "1", MembershipType.STANDARD);
        Member b = lib.registerMember("B", "b@b.kz", "2", MembershipType.STANDARD);
        LibraryItem item = lib.addItem(ItemType.BOOK, "Obs", "X", 2020);
        lib.checkout(a.getId(), item.getId(), LocalDate.now());
        lib.reserve(b.getId(), item.getId());
        lib.returnItem(item.getId(), LocalDate.now());
        check(b.getNotifications().size() == 1, "Observer: reserving member is notified on return");
        check(a.getNotifications().isEmpty(), "Observer: other members are not notified");
    }

    private static void commandUndo() {
        LibraryFacade lib = freshLibrary();
        Member a = lib.registerMember("A", "a@a.kz", "1", MembershipType.STANDARD);
        LibraryItem item = lib.addItem(ItemType.BOOK, "Cmd", "X", 2020);
        lib.checkout(a.getId(), item.getId(), LocalDate.now());
        check(!item.isAvailable() && lib.getLoans().size() == 1, "Checkout makes the item unavailable");
        lib.undoLastAction();
        check(item.isAvailable() && lib.getLoans().isEmpty(), "Undo restores the previous state");
        check(lib.undoLastAction() == null, "Undo on empty history returns null");
    }

    private static void facadeRules() {
        LibraryFacade lib = freshLibrary();
        Member a = lib.registerMember("A", "a@a.kz", "1", MembershipType.STANDARD);
        LibraryItem first = lib.addItem(ItemType.BOOK, "One", "X", 2020);
        lib.addItem(ItemType.BOOK, "Two", "X", 2020);
        lib.addItem(ItemType.BOOK, "Three", "X", 2020);
        LibraryItem fourth = lib.addItem(ItemType.BOOK, "Four", "X", 2020);
        LocalDate today = LocalDate.now();
        lib.checkout(a.getId(), first.getId(), today);
        lib.checkout(a.getId(), lib.search("Two").get(0).getId(), today);
        lib.checkout(a.getId(), lib.search("Three").get(0).getId(), today);
        check(throwsLibraryException(() -> lib.checkout(a.getId(), fourth.getId(), today)),
                "STANDARD member cannot exceed 3 loans");
        check(throwsLibraryException(() -> lib.checkout("MB-999", fourth.getId(), today)), "Unknown member is rejected");
        double fine = lib.returnItem(first.getId(), today.plusDays(14 + 10));
        check(fine == 5.0, "Late return (10 days, book, STANDARD) costs 5.00");
        Loan closed = lib.getLoans().get(0);
        check(!closed.isActive() && closed.getFine() == 5.0, "Closed loan stores the fine");
        check(throwsLibraryException(() -> lib.returnItem(first.getId(), today)), "Returning twice is rejected");
    }

    // ---------------------------------------------------------------- helpers
    private static boolean throwsLibraryException(Runnable action) {
        try {
            action.run();
            return false;
        } catch (LibraryException e) {
            return true;
        }
    }

    private static void check(boolean condition, String name) {
        if (condition) {
            passed++;
            System.out.println("  PASS  " + name);
        } else {
            failed++;
            System.out.println("  FAIL  " + name);
        }
    }
}
