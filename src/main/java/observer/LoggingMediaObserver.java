package observer;

import model.media.Media;
import util.LoggerManager;
import java.util.logging.Logger;

// Observer class for logging media changes
public class LoggingMediaObserver implements MediaChangeObserver {
    private static final Logger LOGGER = LoggerManager.getLogger(LoggingMediaObserver.class.getName());

    @Override
    public void onMediaAdded(Media media) {
        LOGGER.info("Media added: " + media.getTitle() + " (ID: " + media.getId() + ")");
    }

    @Override
    public void onMediaRemoved(Media media) {
        LOGGER.info("Media removed: " + media.getTitle() + " (ID: " + media.getId() + ")");
    }

    @Override
    public void onMediaUpdated(Media oldMedia, Media newMedia) {
        LOGGER.info("Media updated: " + oldMedia.getTitle() + " -> " + newMedia.getTitle() + " (ID: "
                + newMedia.getId() + ")");
    }
}