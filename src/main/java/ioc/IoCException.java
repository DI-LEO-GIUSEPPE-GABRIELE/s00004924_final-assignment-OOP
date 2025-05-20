package ioc;

// Exception thrown by the IoC container when an error occurs during dependency injection
public class IoCException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new IoCException with the specified detail message.
     * 
     * @param message : The detail message
     */
    public IoCException(String message) {
        super(message);
    }

    /**
     * Constructs a new IoCException with the specified detail message and cause.
     * 
     * @param message : The detail message
     * @param cause   : The cause of the exception
     */
    public IoCException(String message, Throwable cause) {
        super(message, cause);
    }
}