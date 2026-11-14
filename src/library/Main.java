package library;

import library.structural.facade.LibraryFacade;
import library.ui.ConsoleMenu;
import library.ui.Demo;

/**
 * Library Management System - entry point.
 * <pre>
 *   java library.Main        -> automatic demo of all patterns
 *   java library.Main menu   -> interactive console menu
 * </pre>
 */
public class Main {
    public static void main(String[] args) {
        LibraryFacade library = new LibraryFacade();
        if (args.length > 0 && args[0].equalsIgnoreCase("menu")) {
            new ConsoleMenu(library).run();
        } else {
            new Demo(library).run();
        }
    }
}
