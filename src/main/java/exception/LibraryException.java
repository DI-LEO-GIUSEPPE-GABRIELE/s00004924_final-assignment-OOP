package exception;

/**
 * Eccezione base per tutte le eccezioni della biblioteca.
 * Parte del pattern Exception Shielding.
 */
public class LibraryException extends Exception {
    
    public LibraryException(String message) {
        super(message);
    }
    
    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }
}