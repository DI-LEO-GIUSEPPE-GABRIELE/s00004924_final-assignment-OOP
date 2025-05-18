package pattern.template;

import model.media.Media;
import java.util.logging.Logger;
import util.LoggerManager;

// Template Method pattern implementation for processing media objects
public abstract class MediaProcessor {
    private static final Logger LOGGER = LoggerManager.getLogger(MediaProcessor.class.getName());

    /**
     * Template method defining the algorithm for processing media
     * 
     * @param media : The media to process
     * @return true or false if processing is successful
     */
    public final boolean processMedia(Media media) {
        LOGGER.info("Starting to process media: " + media.getTitle());

        // Template method steps
        if (!validateMedia(media)) {
            LOGGER.warning("Media validation failed: " + media.getTitle());
            return false;
        }

        prepareMedia(media);
        boolean result = doProcessMedia(media);
        finalizeProcessing(media, result);

        LOGGER.info("Finished processing media: " + media.getTitle() + ", result: " + result);
        return result;
    }

    /**
     * Validate the media before processing
     * Can be overridden by subclasses
     * 
     * @param media : The media to validate
     * @return true or false if the media is valid
     */
    protected boolean validateMedia(Media media) {
        // Default implementation checks for null and basic properties
        return media != null && media.getTitle() != null && !media.getTitle().isEmpty();
    }

    /**
     * Prepare the media for processing
     * Can be overridden by subclasses
     * 
     * @param media : The media to prepare
     */
    protected void prepareMedia(Media media) {
        // Default implementation does nothing
        LOGGER.fine("Preparing media: " + media.getTitle());
    }

    /**
     * Abstract method that must be implemented by subclasses
     * 
     * @param media : The media to process
     * @return true or false if processing is successful
     */
    protected abstract boolean doProcessMedia(Media media);

    /**
     * Finalize the processing of the media
     * Can be overridden by subclasses
     * 
     * @param media   : The media that was processed
     * @param success : If the processing is successful
     */
    protected void finalizeProcessing(Media media, boolean success) {
        if (success) {
            LOGGER.info("Successfully processed media: " + media.getTitle());
        } else {
            LOGGER.warning("Failed to process media: " + media.getTitle());
        }
    }
}