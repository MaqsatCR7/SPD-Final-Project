package library.behavioral.observer;

/** Second concrete observer: writes every availability change to an audit log. */
public class AuditLogObserver implements ItemObserver {
    @Override
    public void update(ItemEvent event) {
        System.out.println("   [AUDIT] " + event.item().getId() + " '" + event.item().getTitle()
                + "' -> " + event.type());
    }
}
