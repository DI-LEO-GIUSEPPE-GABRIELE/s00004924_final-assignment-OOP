package repository;

import exception.LibraryException;
import exception.MediaNotFoundException;
import model.media.Book;
import model.media.Media;
import model.media.MediaCollection;
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

    /**
     * Get instance of repository
     * 
     * @return The instance of repository
     */
    public static synchronized MediaRepository getInstance() {
        if (instance == null) {
            instance = new MediaRepository();
        }
        return instance;
    }

    @Override
    /**
     * Annotation: Override method of the Repository interface
     * Save a media in the repository
     * 
     * @param media - The media to save
     * @return The saved media
     * @throws LibraryException - If the media is null or there is an error during
     *                          the
     *                          save operation
     */
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
    /**
     * Annotation: Override method of the Repository interface
     * Find a media by its ID
     * 
     * @param id - The ID of the media to find
     * @return The found media
     * @throws MediaNotFoundException - If the media with the specified ID is not
     *                                found
     */
    public Media findById(String id) throws MediaNotFoundException {
        if (!mediaMap.containsKey(id)) {
            LOGGER.warning("Media not found with ID: " + id);
            throw new MediaNotFoundException("Media not found with ID: " + id);
        }

        return mediaMap.get(id);
    }

    @Override
    /**
     * Annotation: Override method of the Repository interface
     * Find all media in the repository
     * 
     * @return List of all media
     * @throws LibraryException - If there is an error retrieving the media list
     */
    public List<Media> findAll() throws LibraryException {
        return new ArrayList<>(mediaMap.values());
    }

    @Override
    /**
     * Annotation: Override method of the Repository interface
     * Delete a media by its ID
     * 
     * @param id - The ID of the media to delete
     * @throws MediaNotFoundException - If the media with the specified ID is not
     *                                found
     */
    public void delete(String id) throws MediaNotFoundException {
        if (!mediaMap.containsKey(id)) {
            LOGGER.warning("Impossible to delete: Media not found with ID: " + id);
            throw new MediaNotFoundException("Media not found with ID: " + id);
        }

        // Remove the media from all collections
        Media mediaToDelete = mediaMap.get(id);
        for (Media media : mediaMap.values()) {
            if (media instanceof MediaCollection) {
                MediaCollection collection = (MediaCollection) media;
                List<Media> mediaItems = collection.getMediaItems();
                for (Media item : mediaItems) {
                    // Check if the media is in the collection using the equals method
                    if (item.equals(mediaToDelete)) {
                        collection.removeMedia(mediaToDelete);
                        break;
                    }
                }
            }
        }

        mediaMap.remove(id);
        saveMediaToStorage();
        LOGGER.info("Media deleted with ID: " + id);
    }

    @Override
    /**
     * Annotation: Override method of the Repository interface
     * Update a media in the repository
     * 
     * @param media - The media to update
     * @return The updated media
     * @throws MediaNotFoundException - If the media is null or not found in the
     *                                repository
     */
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

    /**
     * Find media by title
     * 
     * @return The found media
     */
    public List<Media> findByTitle(String title) {
        return mediaMap.values().stream()
                .filter(media -> media.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Find media by author
     * 
     * @return The found media
     */
    public List<Book> findByAuthor(String author) {
        return mediaMap.values().stream()
                .filter(media -> media instanceof Book)
                .map(media -> (Book) media)
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Find media by publication year
     * 
     * @return The found media
     */
    public List<Media> findByPublicationYear(int year) {
        return mediaMap.values().stream()
                .filter(media -> media.getPublicationDate().getYear() == year)
                .collect(Collectors.toList());
    }
}