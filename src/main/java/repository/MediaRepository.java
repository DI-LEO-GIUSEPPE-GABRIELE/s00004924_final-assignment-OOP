package repository;

import exception.LibraryException;
import exception.MediaNotFoundException;
import model.media.Book;
import model.media.Media;
import util.LoggerManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

// Repository class for Media, using memory location for storage
public class MediaRepository implements Repository<Media, String> {
    private static final Logger LOGGER = LoggerManager.getLogger(MediaRepository.class.getName());
    private static MediaRepository instance;
    private final Map<String, Media> mediaMap;

    private MediaRepository() {
        this.mediaMap = new HashMap<>();
        LOGGER.info("Starting the repository for media");
        loadMediaFromStorage();
    }

    // Load media from file system
    private void loadMediaFromStorage() {
        try {
            FileStorageManager storageManager = FileStorageManager.getInstance();
            List<Media> mediaList = storageManager.loadMedia();

            for (Media media : mediaList) {
                mediaMap.put(media.getId(), media);
            }

            LOGGER.info("Media loaded from file system: " + mediaList.size());
        } catch (LibraryException e) {
            LOGGER.warning("Impossible to load Media from file system: " + e.getMessage());
        }
    }

    // Save media in file system
    private void saveMediaToStorage() {
        try {
            FileStorageManager storageManager = FileStorageManager.getInstance();
            storageManager.saveMedia(mediaMap);
            LOGGER.info("Media saved in file system");
        } catch (LibraryException e) {
            LOGGER.warning("Unable to save media to file system: " + e.getMessage());
        }
    }

    // Get instance of repository
    public static synchronized MediaRepository getInstance() {
        if (instance == null) {
            instance = new MediaRepository();
        }
        return instance;
    }

    @Override
    // Override method of the Repository interface
    public Media save(Media media) throws LibraryException {
        if (media == null) {
            throw new LibraryException("Impossible to save a null media");
        }

        mediaMap.put(media.getId(), media);
        saveMediaToStorage();
        LOGGER.info("Media saved with ID: " + media.getId());
        return media;
    }

    @Override
    // Override method of the Repository interface
    public Media findById(String id) throws MediaNotFoundException {
        if (!mediaMap.containsKey(id)) {
            LOGGER.warning("Media not found with ID: " + id);
            throw new MediaNotFoundException("Media not found with ID: " + id);
        }

        return mediaMap.get(id);
    }

    @Override
    // Override method of the Repository interface
    public List<Media> findAll() throws LibraryException {
        return new ArrayList<>(mediaMap.values());
    }

    @Override
    // Override method of the Repository interface
    public void delete(String id) throws MediaNotFoundException {
        if (!mediaMap.containsKey(id)) {
            LOGGER.warning("Impossible to delete: Media not found with ID: " + id);
            throw new MediaNotFoundException("Media not found with ID: " + id);
        }

        mediaMap.remove(id);
        saveMediaToStorage();
        LOGGER.info("Media deleted with ID: " + id);
    }

    @Override
    // Override method of the Repository interface
    public Media update(Media media) throws MediaNotFoundException {
        if (media == null || !mediaMap.containsKey(media.getId())) {
            LOGGER.warning("Impossible to update: Media not found");
            throw new MediaNotFoundException("Media not found");
        }

        mediaMap.put(media.getId(), media);
        saveMediaToStorage();
        LOGGER.info("Media updated with ID: " + media.getId());
        return media;
    }

    // Find media by title
    public List<Media> findByTitle(String title) {
        return mediaMap.values().stream()
                .filter(media -> media.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Find media by author
    public List<Book> findByAuthor(String author) {
        return mediaMap.values().stream()
                .filter(media -> media instanceof Book)
                .map(media -> (Book) media)
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Find media by publication year
    public List<Media> findByPublicationYear(int year) {
        return mediaMap.values().stream()
                .filter(media -> media.getPublicationDate().getYear() == year)
                .collect(Collectors.toList());
    }
}