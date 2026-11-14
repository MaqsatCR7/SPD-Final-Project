package library.creational.singleton;

import library.exception.LibraryException;
import library.model.LibraryItem;
import library.model.Loan;
import library.model.Member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * SINGLETON pattern (initialization-on-demand holder idiom).
 * Exactly one in-memory database exists; the JVM guarantees thread-safe lazy creation
 * of the holder class, so no explicit synchronization is needed.
 */
public final class LibraryDatabase {

    private static final class Holder {
        private static final LibraryDatabase INSTANCE = new LibraryDatabase();
    }

    private final Map<String, LibraryItem> items = new LinkedHashMap<>();
    private final Map<String, Member> members = new LinkedHashMap<>();
    private final List<Loan> loans = new ArrayList<>();

    private LibraryDatabase() {
    }

    public static LibraryDatabase getInstance() {
        return Holder.INSTANCE;
    }

    // ---- items
    public void addItem(LibraryItem item) {
        if (items.containsKey(item.getId())) {
            throw new LibraryException("Item already exists: " + item.getId());
        }
        items.put(item.getId(), item);
    }

    public Optional<LibraryItem> findItem(String id) {
        return Optional.ofNullable(items.get(id));
    }

    public Collection<LibraryItem> getItems() {
        return Collections.unmodifiableCollection(items.values());
    }

    // ---- members
    public void addMember(Member member) {
        members.put(member.getId(), member);
    }

    public Optional<Member> findMember(String id) {
        return Optional.ofNullable(members.get(id));
    }

    public Collection<Member> getMembers() {
        return Collections.unmodifiableCollection(members.values());
    }

    // ---- loans
    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    public void removeLoan(Loan loan) {
        loans.remove(loan);
    }

    public List<Loan> getLoans() {
        return Collections.unmodifiableList(loans);
    }

    public Optional<Loan> findActiveLoanByItem(String itemId) {
        return loans.stream()
                .filter(l -> l.isActive() && l.getItem().getId().equals(itemId))
                .findFirst();
    }

    public long countActiveLoans(Member member) {
        return loans.stream().filter(l -> l.isActive() && l.getMember().equals(member)).count();
    }

    /** Wipes all data (used by tests and demos). */
    public void clear() {
        items.clear();
        members.clear();
        loans.clear();
    }
}
