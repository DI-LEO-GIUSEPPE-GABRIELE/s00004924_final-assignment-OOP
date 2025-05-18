package pattern.template;

import model.media.Media;
import pattern.adapter.MediaAdapter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;
import util.LoggerManager;

// Implementation of the MediaProcessor template
public class BookExportProcessor extends MediaProcessor {
    private static final Logger LOGGER = LoggerManager.getLogger(BookExportProcessor.class.getName());
    private final String exportFormat;
    private final String exportPath;

    /**
     * Create a new BookExportProcessor
     * 
     * @param exportFormat : The format to export to
     * @param exportPath   : The path to export to
     */
    public BookExportProcessor(String exportFormat, String exportPath) {
        this.exportFormat = exportFormat;
        this.exportPath = exportPath;
    }

    @Override
    protected boolean validateMedia(Media media) {
        // First call the parent validation
        if (!super.validateMedia(media)) {
            return false;
        }

        // Additional validation specific to books
        try {
            // Check if it's a book by checking for the author method
            media.getClass().getMethod("getAuthor").invoke(media);
            return true;
        } catch (Exception e) {
            LOGGER.warning("Media is not a book: " + media.getTitle());
            return false;
        }
    }

    @Override
    protected void prepareMedia(Media media) {
        super.prepareMedia(media);
        LOGGER.info("Preparing to export book: " + media.getTitle() + " to " + exportFormat);
    }

    @Override
    protected boolean doProcessMedia(Media media) {
        try {
            // Use the adapter to convert the media to the desired format
            MediaAdapter adapter = new MediaAdapter(media);
            String content = adapter.exportTo(exportFormat);

            // Write the content to a file
            String filename = exportPath + "/" + media.getTitle().replaceAll("\\s+", "_") + "."
                    + exportFormat.toLowerCase();
            try (FileWriter writer = new FileWriter(filename)) {
                writer.write(content);
            }

            LOGGER.info("Successfully exported book to: " + filename);
            return true;
        } catch (IOException e) {
            LOGGER.severe("Error exporting book: " + e.getMessage());
            return false;
        }
    }

    @Override
    protected void finalizeProcessing(Media media, boolean success) {
        super.finalizeProcessing(media, success);
        if (success) {
            LOGGER.info("Book export completed successfully");
        } else {
            LOGGER.warning("Book export failed");
        }
    }
}