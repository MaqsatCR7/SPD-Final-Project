package library.behavioral.command;

/** Command interface of the COMMAND pattern (supports undo). */
public interface Command {
    void execute();

    void undo();

    String describe();
}
