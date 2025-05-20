package processor;

import model.media.Media;

import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import util.LoggerManager;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import adapter.MediaAdapter;

// ExportProcessor class
public class ExportProcessor {
    private static final Logger LOGGER = LoggerManager.getLogger(ExportProcessor.class.getName());
    private final String exportFormat;
    private final String exportPath;

    /**
     * Create a new ExportProcessor
     * 
     * @param exportFormat : The export format (JSON or WORD)
     * @param exportPath   : The path to export the file
     */
    public ExportProcessor(String exportFormat, String exportPath) {
        this.exportFormat = exportFormat;
        this.exportPath = exportPath;
    }

    /**
     * Method to export the media list to the specified format
     * 
     * @param mediaList : The list of media to export
     * @param mediaType : The type of media to export (Book, Magazine, Collection)
     * @return true or false if the export is successful
     */
    public boolean processMediaList(List<Media> mediaList, String mediaType) {
        if (mediaList == null || mediaList.isEmpty()) {
            LOGGER.warning("Nessun media da esportare");
            return false;
        }

        LOGGER.info("Prepare to export in format: " + exportFormat);

        try {
            if (exportFormat.equalsIgnoreCase("JSON")) {
                return exportToJSON(mediaList, mediaType);
            } else if (exportFormat.equalsIgnoreCase("WORD")) {
                return exportToWord(mediaList, mediaType);
            } else {
                LOGGER.warning("Format not supported: " + exportFormat);
                return false;
            }
        } catch (Exception e) {
            LOGGER.severe("Error during export: " + e.getMessage());
            return false;
        }
    }

    private boolean exportToJSON(List<Media> mediaList, String mediaType) throws IOException {
        // Use StringBuilder to build the JSON content
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("[\n");

        for (int i = 0; i < mediaList.size(); i++) {
            Media media = mediaList.get(i);

            // Use MediaAdapter to export to JSON
            MediaAdapter adapter = new MediaAdapter(media);
            String content = adapter.exportTo("JSON");

            // Remove the comma if it's the last element
            if (content.endsWith(",}")) {
                content = content.substring(0, content.length() - 2) + "}";
            }

            // Add the comma if it's not the last element
            if (i < mediaList.size() - 1) {
                contentBuilder.append(content).append(",\n");
            } else {
                contentBuilder.append(content).append("\n");
            }
        }

        contentBuilder.append("]");

        // Write on file
        String filename = exportPath + "/" + mediaType + "_list.json";
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(contentBuilder.toString());
            // Flush the writer to ensure all data is written
            writer.flush();
        }

        System.out.println("Export completed successfully.");
        LOGGER.info("Export completed successfully.");
        return true;
    }

    private boolean exportToWord(List<Media> mediaList, String mediaType) throws IOException {
        XWPFDocument document = new XWPFDocument();

        XWPFParagraph title = document.createParagraph();
        XWPFRun titleRun = title.createRun();
        titleRun.setText("Lists type: " + mediaType);
        titleRun.setBold(true);
        titleRun.setFontSize(16);

        // Every media is a paragraph
        for (Media media : mediaList) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("ID: " + media.getId());
            run.addBreak();
            run.setText("Title: " + media.getTitle());
            run.addBreak();
            run.setText("Publication date: " + media.getPublicationDate());
            run.addBreak();
            run.setText("Available: " + (media.isAvailable() ? "Yes" : "No"));
            run.addBreak();
            run.addBreak();
        }

        String filename = exportPath + "/" + mediaType + "_list.docx";
        try (FileOutputStream out = new FileOutputStream(filename)) {
            document.write(out);
            document.close();
        }

        LOGGER.info("Export completed successfully.");
        return true;
    }
}