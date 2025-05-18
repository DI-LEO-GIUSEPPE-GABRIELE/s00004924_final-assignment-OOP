package pattern.chain;

import model.media.Media;

// Base handler class for the Chain of Responsibility pattern
public abstract class MediaHandler {
    private MediaHandler nextHandler;

    /**
     * Set the next handler in the chain
     * 
     * @param handler : The next handler
     * @return the next handler for chaining
     */
    public MediaHandler setNext(MediaHandler handler) {
        this.nextHandler = handler;
        return handler;
    }

    /**
     * If this handler cannot process the operation, it passes it to the next
     * handler
     * 
     * @param media     : The media to process
     * @param operation : The operation to perform
     * @return true or false if the operation is handled
     */
    public boolean handle(Media media, String operation) {
        if (canHandle(media, operation)) {
            return doHandle(media, operation);
        } else if (nextHandler != null) {
            return nextHandler.handle(media, operation);
        }
        return false;
    }

    /**
     * Check if this handler can handle the operation for the given media
     * 
     * @param media     : The media to check
     * @param operation : The operation to check
     * @return true or false if this handler can handle the operation
     */
    protected abstract boolean canHandle(Media media, String operation);

    /**
     * Handle the operation for the given media
     * 
     * @param media     : The media to handle
     * @param operation : The operation to perform
     * @return true or false if the operation is handled successfully
     */
    protected abstract boolean doHandle(Media media, String operation);
}