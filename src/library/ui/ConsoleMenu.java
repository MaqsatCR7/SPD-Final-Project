package library.ui;

import library.exception.LibraryException;
import library.model.ItemType;
import library.model.Loan;
import library.model.MembershipType;
import library.structural.adapter.LegacyCatalogAdapter;
import library.structural.adapter.LegacyCatalogSystem;
import library.structural.decorator.LoanExtra;
import library.structural.facade.LibraryFacade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Interactive console UI. Talks only to the {@link LibraryFacade}. */
public class ConsoleMenu {
    private final LibraryFacade library;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleMenu(LibraryFacade library) {
        this.library = library;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = ask("Choose");
            if (choice == null) {
                break;
            }
            try {
                running = handle(choice.trim());
            } catch (LibraryException | IllegalArgumentException e) {
                System.out.println("   Error: " + e.getMessage());
            }
        }
        System.out.println("Bye!");
    }

    private boolean handle(String choice) {
        switch (choice) {
            case "1" -> ReportPrinter.printItems(library.getItems());
            case "2" -> ReportPrinter.printMembers(library.getMembers());
            case "3" -> registerMember();
            case "4" -> addItem();
            case "5" -> System.out.println("   Imported items: "
                    + library.importCatalog(new LegacyCatalogAdapter(new LegacyCatalogSystem())));
            case "6" -> checkout();
            case "7" -> returnItem();
            case "8" -> {
                library.reserve(ask("Member id"), ask("Item id"));
                System.out.println("   Reserved. You will be notified when it is available.");
            }
            case "9" -> {
                String undone = library.undoLastAction();
                System.out.println(undone == null ? "   Nothing to undo" : "   Undone: " + undone);
            }
            case "10" -> ReportPrinter.printLoans(library.getLoans());
            case "11" -> ReportPrinter.printHistory(library.getHistoryLog());
            case "12" -> library.search(ask("Search text")).forEach(i -> System.out.println("   " + i));
            case "0" -> {
                return false;
            }
            default -> System.out.println("   Unknown option");
        }
        return true;
    }

    private void registerMember() {
        String name = ask("Name");
        String email = ask("E-mail");
        String phone = ask("Phone");
        MembershipType type = MembershipType.valueOf(
                ask("Membership (STANDARD/STUDENT/PREMIUM)").trim().toUpperCase());
        System.out.println("   Created: " + library.registerMember(name, email, phone, type));
    }

    private void addItem() {
        ItemType type = ItemType.valueOf(ask("Type (BOOK/MAGAZINE/DVD)").trim().toUpperCase());
        String title = ask("Title");
        String creator = ask("Author / publisher / director");
        int year = Integer.parseInt(ask("Year").trim());
        System.out.println("   Created: " + library.addItem(type, title, creator, year));
    }

    private void checkout() {
        String memberId = ask("Member id");
        String itemId = ask("Item id");
        String extrasText = ask("Extras (EXTENDED, SMS_REMINDER, HOME_DELIVERY; comma separated; empty = none)");
        List<LoanExtra> extras = new ArrayList<>();
        if (extrasText != null && !extrasText.isBlank()) {
            for (String part : extrasText.split(",")) {
                extras.add(LoanExtra.valueOf(part.trim().toUpperCase()));
            }
        }
        Loan loan = library.checkout(memberId, itemId, LocalDate.now(), extras.toArray(new LoanExtra[0]));
        System.out.println("   " + loan);
        System.out.printf("   Extras fee: %.2f%n", loan.getExtraFee());
    }

    private void returnItem() {
        String itemId = ask("Item id");
        int days = Integer.parseInt(ask("Days from today (0 = today, 20 = simulate late return)").trim());
        double fine = library.returnItem(itemId, LocalDate.now().plusDays(days));
        System.out.printf("   Returned. Fine: %.2f%n", fine);
    }

    private void printMenu() {
        System.out.println();
        System.out.println("===== LIBRARY MANAGEMENT SYSTEM =====");
        System.out.println(" 1) List items        2) List members     3) Register member");
        System.out.println(" 4) Add item          5) Import legacy    6) Check out");
        System.out.println(" 7) Return item       8) Reserve item     9) Undo last action");
        System.out.println("10) Loans            11) Command history 12) Search");
        System.out.println(" 0) Exit");
    }

    private String ask(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.hasNextLine() ? scanner.nextLine() : null;
    }
}
