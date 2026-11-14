package library.exception;

/** Single business-rule exception for the whole library domain. */
public class LibraryException extends RuntimeException {
    public LibraryException(String message) {
        super(message);
    }
}
