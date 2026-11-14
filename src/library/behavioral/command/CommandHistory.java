package library.behavioral.command;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/** Invoker of the COMMAND pattern: executes commands and keeps a stack for undo. */
public class CommandHistory {
    private final Deque<Command> undoStack = new ArrayDeque<>();
    private final List<String> log = new ArrayList<>();

    public void execute(Command command) {
        command.execute(); // if it throws, nothing is recorded
        undoStack.push(command);
        log.add("DO   " + command.describe());
    }

    /** @return the description of the undone command, or null when there is nothing to undo */
    public String undoLast() {
        if (undoStack.isEmpty()) {
            return null;
        }
        Command command = undoStack.pop();
        command.undo();
        log.add("UNDO " + command.describe());
        return command.describe();
    }

    public List<String> getLog() {
        return Collections.unmodifiableList(log);
    }
}
