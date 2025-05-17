package repository;

import exception.LibraryException;
import model.media.Media;
import util.LoggerManager;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

// Class for storage of media data management.
public class FileStorageManager {
    private static final Logger LOGGER = LoggerManager.getLogger(FileStorageManager.class.getName());
    private static final String DATA_DIRECTORY = "data";
    private static final String MEDIA_FILE = DATA_DIRECTORY + File.separator + "media.dat";
    private static FileStorageManager instance;

    private FileStorageManager() {
        // Create the data directory if it doesn't exist
        File dataDir = new File(DATA_DIRECTORY);
        if (!dataDir.exists()) {
            if (dataDir.mkdir()) {
                LOGGER.info("Data directory created successfully: " + dataDir.getAbsolutePath());
            } else {
                LOGGER.warning("Unable to create data directory: " + dataDir.getAbsolutePath());
            }
        }
    }

    // Get the instance of the storage manager
    public static synchronized FileStorageManager getInstance() {
        if (instance == null) {
            instance = new FileStorageManager();
        }
        return instance;
    }

    // Save media to file
    public void saveMedia(Map<String, Media> mediaMap) throws LibraryException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MEDIA_FILE))) {
            oos.writeObject(new ArrayList<>(mediaMap.values()));
            LOGGER.info("Media saved to file: " + MEDIA_FILE);
        } catch (IOException e) {
            LOGGER.severe("Error saving media to file: " + e.getMessage());
            throw new LibraryException("Error saving media to file: " + e.getMessage());
        }
    }

    // Load media from file
    @SuppressWarnings("unchecked")
    // Annotation: SuppressWarnings
    public List<Media> loadMedia() throws LibraryException {
        File file = new File(MEDIA_FILE);
        if (!file.exists()) {
            LOGGER.info("Media file not found, a new file will be created on first save.");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Media> mediaList = (List<Media>) ois.readObject();
            LOGGER.info("Media uploaded from files: " + MEDIA_FILE + ", number of media: " + mediaList.size());
            return mediaList;
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.severe("Error loading media from file: " + e.getMessage());
            throw new LibraryException("Error loading media from file: " + e.getMessage());
        }
    }
}