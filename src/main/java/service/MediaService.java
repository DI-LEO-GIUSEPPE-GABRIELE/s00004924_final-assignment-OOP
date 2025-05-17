package service;

import exception.LibraryException;
import exception.MediaNotFoundException;
import model.media.Book;
import model.media.Media;
import model.media.MediaCollection;
import repository.MediaRepository;
import util.LoggerManager;
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
     * @param media - The media to save
     * @return The saved media
     * @throws LibraryException - If there is an error during the save operation
     */
    public Media saveMedia(Media media) throws LibraryException {
        LOGGER.info("Media Save Request: " + media.getTitle());
        return mediaRepository.save(media);
    }

    /**
     * Find media by ID
     * 
     * @param id - The ID of the media to find
     * @return The found media
     * @throws MediaNotFoundException - If the media with the specified ID is not
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
     * @throws LibraryException - If there is an error retrieving the media list
     */
    public List<Media> findAllMedia() throws LibraryException {
        LOGGER.info("All media request");
        return mediaRepository.findAll();
    }

    /**
     * Delete media
     * 
     * @param id - The ID of the media to delete
     * @throws MediaNotFoundException - If the media with the specified ID is not
     *                                found
     */
    public void deleteMedia(String id) throws MediaNotFoundException {
        LOGGER.info("Media Delete Request with ID: " + id);
        mediaRepository.delete(id);
    }

    /**
     * Update media
     * 
     * @param media - The media to update
     * @return The updated media
     * @throws MediaNotFoundException - If the media to update is not found
     */
    public Media updateMedia(Media media) throws MediaNotFoundException {
        LOGGER.info("Media Update Request: " + media.getTitle());
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
     * Find media by author
     * 
     * @return The found media
     */
    public List<Book> findBooksByAuthor(String author) {
        LOGGER.info("Search books by author: " + author);
        return mediaRepository.findByAuthor(author);
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
     * @param collectionId - The ID of the collection
     * @param mediaId      - The ID of the media to add
     * @throws MediaNotFoundException - If the collection or media with the
     *                                specified
     *                                ID is not found
     * @throws LibraryException       - If the specified ID does not match a
     *                                collection
     */
    public void addMediaToCollection(String collectionId, String mediaId)
            throws MediaNotFoundException, LibraryException {
        Media collection = mediaRepository.findById(collectionId);
        Media media = mediaRepository.findById(mediaId);

        if (!(collection instanceof MediaCollection)) {
            throw new LibraryException("The specified ID does not match a collection");
        }

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
     * @param collectionId - The ID of the collection
     * @param mediaId      - The ID of the media to remove
     * @throws MediaNotFoundException - If the collection or media with the
     *                                specified
     *                                ID is not found
     * @throws LibraryException       - If the specified ID does not match a
     *                                collection
     */
    public void removeMediaFromCollection(String collectionId, String mediaId)
            throws MediaNotFoundException, LibraryException {
        Media collection = mediaRepository.findById(collectionId);
        Media media = mediaRepository.findById(mediaId);

        if (!(collection instanceof MediaCollection)) {
            throw new LibraryException("The specified ID does not match a collection");
        }

        // Set media as available when removed from a collection
        media.setAvailable(true);
        mediaRepository.update(media);

        ((MediaCollection) collection).removeMedia(media);
        mediaRepository.update(collection);
        LOGGER.info("Media " + mediaId + " removed from collection " + collectionId);
    }
}