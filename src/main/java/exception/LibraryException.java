package exception;

// Base exception class for all library exceptions
public class LibraryException extends Exception {

    public LibraryException(String message) {
        super(message);
    }
}