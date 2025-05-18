package pattern.chain;

import model.media.Media;
import java.util.logging.Logger;
import util.LoggerManager;

// Handler for book operations in the Chain of Responsibility pattern
public class BookOperationHandler extends MediaHandler {
    private static final Logger LOGGER = LoggerManager.getLogger(BookOperationHandler.class.getName());

    @Override
    protected boolean canHandle(Media media, String operation) {
        // Check if the media is a book by checking for the author method
        try {
            media.getClass().getMethod("getAuthor").invoke(media);
            // Check if the operation is one that this handler can handle
            return operation.equalsIgnoreCase("print") ||
                    operation.equalsIgnoreCase("export") ||
                    operation.equalsIgnoreCase("validate");
        } catch (Exception e) {
            // Not a book or method doesn't exist
            return false;
        }
    }

    @Override
    protected boolean doHandle(Media media, String operation) {
        try {
            String author = (String) media.getClass().getMethod("getAuthor").invoke(media);
            int pages = (int) media.getClass().getMethod("getPages").invoke(media);

            switch (operation.toLowerCase()) {
                case "print":
                    LOGGER.info("Printing book: " + media.getTitle() + " by " + author + ", " + pages + " pages");
                    return true;
                case "export":
                    LOGGER.info("Exporting book: " + media.getTitle() + " by " + author);
                    return true;
                case "validate":
                    boolean valid = author != null && !author.isEmpty() && pages > 0;
                    LOGGER.info("Validating book: " + media.getTitle() + ", valid: " + valid);
                    return valid;
                default:
                    LOGGER.warning("Unknown operation: " + operation);
                    return false;
            }
        } catch (Exception e) {
            LOGGER.severe("Error handling book operation: " + e.getMessage());
            return false;
        }
    }
}