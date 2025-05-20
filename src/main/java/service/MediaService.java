package service;

import exception.LibraryException;
import exception.MediaNotFoundException;
import model.media.Media;
import model.media.MediaCollection;
import repository.MediaRepository;
import util.LoggerManager;
import memento.MediaMementoService;
import memento.MediaMemento;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

// Service for media management
public class MediaService {
    private static final Logger LOGGER = LoggerManager.getLogger(MediaService.class.getName());
    private static MediaService instance;
    private final MediaRepository mediaRepository;

    private MediaService() {
        this.mediaRepository = MediaRepository.getInstance();
        LOGGER.info("Starting the media service");
    }

    /**
     * Get instance of the service
     * 
     * @return The instance of the service
     */
    public static synchronized MediaService getInstance() {
        if (instance == null) {
            instance = new MediaService();
        }
        return instance;
    }

    /**
     * Save media in repository
     * 
     * @param media : The media to save
     * @return The saved media
     * @throws LibraryException : If there is an error during the save operation
     */
    public Media saveMedia(Media media) throws LibraryException {
        LOGGER.info("Media Save Request: " + media.getTitle());
        return mediaRepository.save(media);
    }

    /**
     * Restore the previous state of a media
     * 
     * @param mediaId : The ID of the media to restore
     * @return The restored media or null if no previous state exists
     * @throws MediaNotFoundException : If the media with the specified ID is not
     *                                found
     */
    public Media restoreMediaChanges(String mediaId) throws MediaNotFoundException {
        LOGGER.info("Restore Request for Media with ID: " + mediaId);

        MediaMementoService careTaker = MediaMementoService.getInstance();

        if (!careTaker.hasPreviousState(mediaId)) {
            LOGGER.warning("No previous state found for media with ID: " + mediaId);
            return null;
        }

        MediaMemento memento = careTaker.getLastState(mediaId);
        if (memento == null) {
            LOGGER.warning("Failed to retrieve previous state for media with ID: " + mediaId);
            return null;
        }

        try {
            // Check if the media exists
            Media currentMedia = mediaRepository.findById(mediaId);
            String mediaType = currentMedia.getClass().getSimpleName();

            Media restoredMedia;

            if (mediaType.equals("Book")) {
                // Restore the state of the Book
                String title = memento.getTitle();
                LocalDate publicationDate = memento.getPublicationDate();
                String author = (String) memento.getState("author");
                Integer pages = (Integer) memento.getState("pages");
                String publisher = (String) currentMedia.getClass().getMethod("getPublisher").invoke(currentMedia);

                // Create a new instance of Book with the restored state
                restoredMedia = (Media) Class.forName("model.media.Book")
                        .getConstructor(String.class, String.class, String.class, LocalDate.class, String.class,
                                int.class)
                        .newInstance(currentMedia.getId(), title, author, publicationDate, publisher, pages);

            } else if (mediaType.equals("Magazine")) {
                // Restore the state of the Magazine
                String title = memento.getTitle();
                LocalDate publicationDate = memento.getPublicationDate();
                Integer issue = (Integer) memento.getState("issue");
                String publisher = (String) currentMedia.getClass().getMethod("getPublisher").invoke(currentMedia);

                // Create a new instance of Magazine with the restored state
                restoredMedia = (Media) Class.forName("model.media.Magazine")
                        .getConstructor(String.class, String.class, LocalDate.class, String.class, int.class)
                        .newInstance(currentMedia.getId(), title, publicationDate, publisher, issue);

            } else {
                LOGGER.warning("Unsupported media type for restoration: " + mediaType);
                return null;
            }

            // Restore the availability state
            restoredMedia.setAvailable(currentMedia.isAvailable());

            // Save the restored media
            return mediaRepository.update(restoredMedia);

        } catch (Exception e) {
            LOGGER.severe("Error restoring media state: " + e.getMessage());
            throw new MediaNotFoundException("Error restoring media state: " + e.getMessage());
        }
    }

    /**
     * Find media by ID
     * 
     * @param id : The ID of the media to find
     * @return The found media
     * @throws MediaNotFoundException : If the media with the specified ID is not
     *                                found
     */
    public Media findMediaById(String id) throws MediaNotFoundException {
        LOGGER.info("Media Search by ID: " + id);
        return mediaRepository.findById(id);
    }

    /**
     * Find all media
     * 
     * @return List of all media
     * @throws LibraryException : If there is an error retrieving the media list
     */
    public List<Media> findAllMedia() throws LibraryException {
        LOGGER.info("All media request");
        return mediaRepository.findAll();
    }

    /**
     * Delete media
     * 
     * @param id : The ID of the media to delete
     * @throws MediaNotFoundException : If the media with the specified ID is not
     *                                found
     */
    public void deleteMedia(String id) throws MediaNotFoundException {
        LOGGER.info("Media Delete Request with ID: " + id);
        // Save state before deleting
        MediaMementoService.getInstance().saveState(mediaRepository.findById(id));
        mediaRepository.delete(id);
    }

    /**
     * Update media
     * 
     * @param media : The media to update
     * @return The updated media
     * @throws MediaNotFoundException : If the media to update is not found
     */
    public Media updateMedia(Media media) throws MediaNotFoundException {
        LOGGER.info("Media Update Request: " + media.getTitle());
        // Save the current state before updating
        MediaMementoService.getInstance().saveState(mediaRepository.findById(media.getId()));
        return mediaRepository.update(media);
    }

    /**
     * Find media by title
     * 
     * @return The found media
     */
    public List<Media> findMediaByTitle(String title) {
        LOGGER.info("Media search by title: " + title);
        return mediaRepository.findByTitle(title);
    }

    /**
     * Find media by publication year
     * 
     * @return The found media
     */
    public List<Media> findMediaByPublicationYear(int year) {
        LOGGER.info("Search media by publication year: " + year);
        return mediaRepository.findByPublicationYear(year);
    }

    /**
     * Add media to collection
     * 
     * @param collectionId : The ID of the collection
     * @param mediaId      : The ID of the media to add
     * @throws MediaNotFoundException : If the collection or media with the
     *                                specified
     *                                ID is not found
     * @throws LibraryException       : If the specified ID does not match a
     *                                collection
     */
    public void addMediaToCollection(String collectionId, String mediaId)
            throws MediaNotFoundException, LibraryException {
        Media collection = mediaRepository.findById(collectionId);
        Media media = mediaRepository.findById(mediaId);

        if (!(collection instanceof MediaCollection)) {
            throw new LibraryException("The specified ID does not match a collection");
        }

        // Save states before modifying
        MediaMementoService.getInstance().saveState(collection);
        MediaMementoService.getInstance().saveState(media);

        // Set media as unavailable when added to a collection
        media.setAvailable(false);
        mediaRepository.update(media);

        ((MediaCollection) collection).addMedia(media);
        mediaRepository.update(collection);
        LOGGER.info("Media " + mediaId + " added to collection " + collectionId);
    }

    /**
     * Remove media from collection
     * 
     * @param collectionId : The ID of the collection
     * @param mediaId      : The ID of the media to remove
     * @throws MediaNotFoundException : If the collection or media with the
     *                                specified
     *                                ID is not found
     * @throws LibraryException       : If the specified ID does not match a
     *                                collection
     */
    public void removeMediaFromCollection(String collectionId, String mediaId)
            throws MediaNotFoundException, LibraryException {
        Media collection = mediaRepository.findById(collectionId);
        Media media = mediaRepository.findById(mediaId);

        if (!(collection instanceof MediaCollection)) {
            throw new LibraryException("The specified ID does not match a collection");
        }

        // Save states before modifying
        MediaMementoService.getInstance().saveState(collection);
        MediaMementoService.getInstance().saveState(media);

        // Set media as available when removed from a collection
        media.setAvailable(true);
        mediaRepository.update(media);

        ((MediaCollection) collection).removeMedia(media);
        mediaRepository.update(collection);
        LOGGER.info("Media " + mediaId + " removed from collection " + collectionId);
    }
}