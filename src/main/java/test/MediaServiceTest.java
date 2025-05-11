package test;

import exception.LibraryException;
import exception.MediaNotFoundException;
import factory.MediaFactory;
import model.media.Book;
import model.media.Media;
import service.MediaService;
import model.media.MediaCollection;
import util.LoggerManager;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

// Test class for MediaService, check the correct operation of CRUD operations and searches.
public class MediaServiceTest {
    private static final Logger LOGGER = LoggerManager.getLogger(MediaServiceTest.class.getName());
    private final MediaService mediaService;

    public MediaServiceTest() {
        this.mediaService = MediaService.getInstance();
    }

    // Run the tests
    public void runTests() {
        LOGGER.info("Starting the test of MediaService");

        try {
            // Creation and saving test
            testCreateAndSave();

            // Search test
            testSearch();

            // Update test
            testUpdate();

            // Delete test
            testDelete();

            // Collections test
            testCollections();

            LOGGER.info("All tests completed successfully");
        } catch (Exception e) {
            LOGGER.severe("Error executing tests: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Creation and saving test
    private void testCreateAndSave() throws LibraryException {
        LOGGER.info("Creation and saving test");

        // Creating and saving a book
        Media book = MediaFactory.createBook(
                "Il Nome della Rosa",
                "Umberto Eco",
                LocalDate.of(1980, 10, 15),
                "Bompiani",
                512);

        mediaService.saveMedia(book);
        LOGGER.info("Book created and saved: " + book.getDetails());

        // Creating and saving a magazine
        Media magazine = MediaFactory.createMagazine(
                "Focus",
                LocalDate.of(2023, 5, 10),
                "Mondadori",
                350);

        mediaService.saveMedia(magazine);
        LOGGER.info("Magazine created and saved: " + magazine.getDetails());

        List<Media> allMedia = mediaService.findAllMedia();
        LOGGER.info("Number of media saved: " + allMedia.size());

        if (allMedia.size() < 2) {
            throw new LibraryException("Error saving media");
        }
    }

    // Search Test
    private void testSearch() throws LibraryException {
        LOGGER.info("Search Test");

        // Ricerca per titolo
        List<Media> mediaByTitle = mediaService.findMediaByTitle("Rosa");
        LOGGER.info("Media found for title 'Rosa': " + mediaByTitle.size());

        // Ricerca per autore
        List<Book> booksByAuthor = mediaService.findBooksByAuthor("Eco");
        LOGGER.info("Books found by author 'Eco': " + booksByAuthor.size());

        // Ricerca per anno
        List<Media> mediaByYear = mediaService.findMediaByPublicationYear(1980);
        LOGGER.info("Media found for year 1980: " + mediaByYear.size());
    }

    // Update Test
    private void testUpdate() throws LibraryException, MediaNotFoundException {
        LOGGER.info("Update Test");

        // Find a book to update
        List<Media> books = mediaService.findMediaByTitle("Rosa");
        if (!books.isEmpty()) {
            Media book = books.get(0);
            book.setAvailable(false);
            mediaService.updateMedia(book);
            LOGGER.info("Updated book: " + book.getDetails());

            Media updatedBook = mediaService.findMediaById(book.getId());
            if (updatedBook.isAvailable()) {
                throw new LibraryException("Error updating book");
            }
            LOGGER.info("Update verification completed successfully");
        }
    }

    // Delete Test
    private void testDelete() throws LibraryException, MediaNotFoundException {
        LOGGER.info("Delete Test");

        // Create a new media to delete
        Media mediaToDelete = MediaFactory.createBook(
                "Libro da eliminare",
                "Autore Test",
                LocalDate.now(),
                "Editore Test",
                100);

        mediaService.saveMedia(mediaToDelete);
        LOGGER.info("Media created for elimination test: " + mediaToDelete.getDetails());

        // Delete the media
        mediaService.deleteMedia(mediaToDelete.getId());
        LOGGER.info("Media deleted");

        try {
            mediaService.findMediaById(mediaToDelete.getId());
            throw new LibraryException("Error deleting media");
        } catch (MediaNotFoundException e) {
            LOGGER.info("Verification deletion completed successfully");
        }
    }

    // Collections Test
    private void testCollections() throws LibraryException, MediaNotFoundException {
        LOGGER.info("Collections Test");

        // Create a new collection
        MediaCollection collection = MediaFactory.createMediaCollection("Test Collection");
        mediaService.saveMedia(collection);
        LOGGER.info("Collection created: " + collection.getDetails());

        // Create a new book to add to the collection
        Media bookForCollection = MediaFactory.createBook(
                "Libro prova per collezione",
                "Autore prova Collezione",
                LocalDate.now(),
                "Editore prova Collezione",
                200);

        mediaService.saveMedia(bookForCollection);
        LOGGER.info("Book created for the collection: " + bookForCollection.getDetails());

        // Add the book to the collection
        mediaService.addMediaToCollection(collection.getId(), bookForCollection.getId());
        LOGGER.info("Book added to collection");

        Media updatedCollection = mediaService.findMediaById(collection.getId());
        if (!(updatedCollection instanceof MediaCollection)) {
            throw new LibraryException("Error retrieving collection");
        }

        MediaCollection retrievedCollection = (MediaCollection) updatedCollection;
        if (retrievedCollection.getMediaItems().isEmpty()) {
            throw new LibraryException("Error adding book to collection");
        }

        LOGGER.info("Number of media in the collection: " + retrievedCollection.getMediaItems().size());
        LOGGER.info("Collection verification completed successfully");
    }

    // Main method for testing
    public static void main(String[] args) {
        MediaServiceTest test = new MediaServiceTest();
        test.runTests();
    }
}