package library.behavioral.command;

import library.model.LibraryItem;
import library.model.Member;
import library.service.LibraryService;

public class ReserveCommand implements Command {
    private final LibraryService service;
    private final Member member;
    private final LibraryItem item;

    public ReserveCommand(LibraryService service, Member member, LibraryItem item) {
        this.service = service;
        this.member = member;
        this.item = item;
    }

    @Override
    public void execute() {
        service.reserve(member, item);
    }

    @Override
    public void undo() {
        service.cancelReservation(member, item);
    }

    @Override
    public String describe() {
        return "Reserve '" + item.getTitle() + "' for " + member.getName();
    }
}
