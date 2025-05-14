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

            // Update availability test
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

        // Search by title
        List<Media> mediaByTitle = mediaService.findMediaByTitle("Rosa");
        LOGGER.info("Media found for title 'Rosa': " + mediaByTitle.size());

        // Search by author
        List<Book> booksByAuthor = mediaService.findBooksByAuthor("Eco");
        LOGGER.info("Books found by author 'Eco': " + booksByAuthor.size());

        // Search by year
        List<Media> mediaByYear = mediaService.findMediaByPublicationYear(1980);
        LOGGER.info("Media found for year 1980: " + mediaByYear.size());
    }

    // Update availability Test
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
                "Book to delete",
                "Author Test",
                LocalDate.now(),
                "Publisher Test",
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
                "Test book for collection",
                "Test Author Collection",
                LocalDate.now(),
                "Test Publisher Collection",
                200);

        mediaService.saveMedia(bookForCollection);
        LOGGER.info("Book created for the collection: " + bookForCollection.getDetails());

        // Create a second book to add to the collection
        Media secondBookForCollection = MediaFactory.createBook(
                "Pirati dei Caraibi",
                "Menichella",
                LocalDate.of(2007, 05, 24),
                "Disney Libri",
                523);

        mediaService.saveMedia(secondBookForCollection);
        LOGGER.info("Second book created for the collection: " + secondBookForCollection.getDetails());

        // Add the books to the collection
        mediaService.addMediaToCollection(collection.getId(), bookForCollection.getId());
        LOGGER.info("First book added to collection");

        mediaService.addMediaToCollection(collection.getId(), secondBookForCollection.getId());
        LOGGER.info("Second book added to collection");

        // Verify the books were added to the collection
        Media updatedCollection = mediaService.findMediaById(collection.getId());
        if (!(updatedCollection instanceof MediaCollection)) {
            throw new LibraryException("Error retrieving collection");
        }

        MediaCollection retrievedCollection = (MediaCollection) updatedCollection;
        if (retrievedCollection.getMediaItems().size() != 2) {
            throw new LibraryException("Error adding books to collection");
        }

        LOGGER.info("Number of media in the collection: " + retrievedCollection.getMediaItems().size());

        // Verify the books are not available
        Media retrievedBook = mediaService.findMediaById(bookForCollection.getId());
        if (retrievedBook.isAvailable()) {
            throw new LibraryException("Book should not be available when added to a collection");
        }

        // Test removing a book from the collection
        mediaService.removeMediaFromCollection(collection.getId(), bookForCollection.getId());
        LOGGER.info("Book removed from collection");

        // Verify the book was removed
        updatedCollection = mediaService.findMediaById(collection.getId());
        retrievedCollection = (MediaCollection) updatedCollection;
        if (retrievedCollection.getMediaItems().size() != 1) {
            throw new LibraryException("Error removing book from collection");
        }

        // Verify the book is now available
        retrievedBook = mediaService.findMediaById(bookForCollection.getId());
        if (!retrievedBook.isAvailable()) {
            throw new LibraryException("Book should be available after being removed from a collection");
        }

        LOGGER.info("Collection verification completed successfully");
    }

    // Main method for testing
    public static void main(String[] args) {
        MediaServiceTest test = new MediaServiceTest();
        test.runTests();
    }
}