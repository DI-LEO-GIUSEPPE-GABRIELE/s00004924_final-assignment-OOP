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

    // Get instance of the service
    public static synchronized MediaService getInstance() {
        if (instance == null) {
            instance = new MediaService();
        }
        return instance;
    }

    // Save media in repository
    public Media saveMedia(Media media) throws LibraryException {
        LOGGER.info("Media Save Request: " + media.getTitle());
        return mediaRepository.save(media);
    }

    // Find media by ID
    public Media findMediaById(String id) throws MediaNotFoundException {
        LOGGER.info("Media Search by ID: " + id);
        return mediaRepository.findById(id);
    }

    // Find all media
    public List<Media> findAllMedia() throws LibraryException {
        LOGGER.info("All media request");
        return mediaRepository.findAll();
    }

    // Delete media
    public void deleteMedia(String id) throws MediaNotFoundException {
        LOGGER.info("Media Delete Request with ID: " + id);
        mediaRepository.delete(id);
    }

    // Update media
    public Media updateMedia(Media media) throws MediaNotFoundException {
        LOGGER.info("Media Update Request: " + media.getTitle());
        return mediaRepository.update(media);
    }

    // Find media by title
    public List<Media> findMediaByTitle(String title) {
        LOGGER.info("Media search by title: " + title);
        return mediaRepository.findByTitle(title);
    }

    // Find books by author
    public List<Book> findBooksByAuthor(String author) {
        LOGGER.info("Search books by author: " + author);
        return mediaRepository.findByAuthor(author);
    }

    // Find media by publication year
    public List<Media> findMediaByPublicationYear(int year) {
        LOGGER.info("Search media by publication year: " + year);
        return mediaRepository.findByPublicationYear(year);
    }

    // Add media to collection
    public void addMediaToCollection(String collectionId, String mediaId)
            throws MediaNotFoundException, LibraryException {
        Media collection = mediaRepository.findById(collectionId);
        Media media = mediaRepository.findById(mediaId);

        if (!(collection instanceof MediaCollection)) {
            throw new LibraryException("The specified ID does not match a collection");
        }

        ((MediaCollection) collection).addMedia(media);
        mediaRepository.update(collection);
        LOGGER.info("Media " + mediaId + " added to collection " + collectionId);
    }

    // Remove media from collection
    public void removeMediaFromCollection(String collectionId, String mediaId)
            throws MediaNotFoundException, LibraryException {
        Media collection = mediaRepository.findById(collectionId);
        Media media = mediaRepository.findById(mediaId);

        if (!(collection instanceof MediaCollection)) {
            throw new LibraryException("The specified ID does not match a collection");
        }

        ((MediaCollection) collection).removeMedia(media);
        mediaRepository.update(collection);
        LOGGER.info("Media " + mediaId + " removed from collection " + collectionId);
    }
}