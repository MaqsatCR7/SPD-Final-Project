package library.structural.facade;

import library.behavioral.command.CheckoutCommand;
import library.behavioral.command.CommandHistory;
import library.behavioral.command.ReserveCommand;
import library.behavioral.command.ReturnCommand;
import library.behavioral.observer.AuditLogObserver;
import library.behavioral.observer.ItemObserver;
import library.creational.factory.ItemFactory;
import library.creational.singleton.LibraryDatabase;
import library.exception.LibraryException;
import library.model.ItemType;
import library.model.LibraryItem;
import library.model.Loan;
import library.model.Member;
import library.model.MembershipType;
import library.service.LibraryService;
import library.structural.adapter.CatalogProvider;
import library.structural.decorator.LoanExtra;
import library.structural.decorator.LoanPolicies;
import library.structural.decorator.LoanPolicy;
import library.util.IdGenerator;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * FACADE pattern: one simple entry point that hides the Singleton database, the Factory,
 * the Builder, the Decorators, the Commands, the Observers and the service layer.
 * The UI (console menu / demo) talks only to this class.
 */
public class LibraryFacade {
    private final LibraryDatabase db = LibraryDatabase.getInstance();
    private final LibraryService service = new LibraryService(db);
    private final CommandHistory history = new CommandHistory();
    private final ItemObserver auditLog = new AuditLogObserver();

    // ------------------------------------------------------------ registration
    public Member registerMember(String name, String email, String phone, MembershipType type) {
        Member member = new Member.Builder(name, email)   // BUILDER
                .phone(phone)
                .membershipType(type)
                .build();
        db.addMember(member);
        return member;
    }

    public LibraryItem addItem(ItemType type, String title, String creator, int year) {
        String id = IdGenerator.next(prefix(type));
        LibraryItem item = ItemFactory.create(type, id, title, creator, year);   // FACTORY METHOD
        register(item);
        return item;
    }

    public int importCatalog(CatalogProvider provider) {   // ADAPTER is hidden behind the interface
        int count = 0;
        for (LibraryItem item : provider.fetchItems()) {
            register(item);
            count++;
        }
        return count;
    }

    private void register(LibraryItem item) {
        db.addItem(item);
        item.subscribe(auditLog);   // OBSERVER
    }

    private String prefix(ItemType type) {
        return switch (type) {
            case BOOK -> "BK";
            case MAGAZINE -> "MG";
            case DVD -> "DV";
        };
    }

    // ------------------------------------------------------------ operations
    public Loan checkout(String memberId, String itemId, LocalDate date, LoanExtra... extras) {
        Member member = getMember(memberId);
        LibraryItem item = getItem(itemId);
        LoanPolicy policy = LoanPolicies.build(item, extras);   // DECORATOR
        CheckoutCommand command = new CheckoutCommand(service, member, item, policy, date);
        history.execute(command);   // COMMAND
        return command.getLoan();
    }

    /** @return the fine to pay (calculated with the STRATEGY of the member's membership) */
    public double returnItem(String itemId, LocalDate date) {
        Loan loan = db.findActiveLoanByItem(itemId)
                .orElseThrow(() -> new LibraryException("No active loan for item " + itemId));
        ReturnCommand command = new ReturnCommand(service, loan, date);
        history.execute(command);
        return command.getFine();
    }

    public void reserve(String memberId, String itemId) {
        history.execute(new ReserveCommand(service, getMember(memberId), getItem(itemId)));
    }

    /** @return description of the undone action or null if there is nothing to undo */
    public String undoLastAction() {
        return history.undoLast();
    }

    // ------------------------------------------------------------ queries
    public List<LibraryItem> search(String text) {
        String q = text.toLowerCase(Locale.ROOT);
        return db.getItems().stream()
                .filter(i -> i.getTitle().toLowerCase(Locale.ROOT).contains(q)
                        || i.getCreator().toLowerCase(Locale.ROOT).contains(q))
                .toList();
    }

    public Collection<LibraryItem> getItems() {
        return db.getItems();
    }

    public Collection<Member> getMembers() {
        return db.getMembers();
    }

    public List<Loan> getLoans() {
        return db.getLoans();
    }

    public List<String> getHistoryLog() {
        return history.getLog();
    }

    private Member getMember(String id) {
        return db.findMember(id).orElseThrow(() -> new LibraryException("Member not found: " + id));
    }

    private LibraryItem getItem(String id) {
        return db.findItem(id).orElseThrow(() -> new LibraryException("Item not found: " + id));
    }
}
