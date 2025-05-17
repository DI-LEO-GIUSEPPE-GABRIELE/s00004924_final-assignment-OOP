package service;

import exception.LibraryException;
import exception.MediaNotFoundException;
import factory.MediaFactory;
import model.media.Book;
import model.media.Media;
import model.media.MediaCollection;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.Assert.*;

// Class for MediaServiceTest
public class MediaServiceJUnitTest {

    private MediaService mediaService;
    private Media testBook;
    private Media testMagazine;

    @Before
    // Annotation: Before
    // Executed before each test
    public void setUp() throws LibraryException {
        mediaService = MediaService.getInstance();

        // Create one book and one magazine for testing
        testBook = MediaFactory.createBook(
                "Test Book",
                "Test Author",
                LocalDate.now(),
                "Test Publisher",
                100);

        testMagazine = MediaFactory.createMagazine(
                "Test Magazine",
                LocalDate.now(),
                "Test Publisher",
                1);

        // Save the media
        mediaService.saveMedia(testBook);
        mediaService.saveMedia(testMagazine);
    }

    @Test
    // Annotation: Test
    // Unit Test
    // Test that the media is saved and can be found by ID
    public void testSaveAndFindById() throws LibraryException, MediaNotFoundException {
        Media foundBook = mediaService.findMediaById(testBook.getId());
        Media foundMagazine = mediaService.findMediaById(testMagazine.getId());

        assertNotNull("Book not found", foundBook);
        assertNotNull("Magazine not found", foundMagazine);
        assertEquals("Book ID does not match", testBook.getId(), foundBook.getId());
        assertEquals("Magazine ID does not match", testMagazine.getId(), foundMagazine.getId());
    }

    @Test
    // Annotation: Test
    // Unit Test
    // Test that all media can be found
    public void testFindAll() throws LibraryException {
        List<Media> allMedia = mediaService.findAllMedia();
        assertNotNull("The media list is void", allMedia);
        assertFalse("The media list is void", allMedia.isEmpty());
    }

    @Test
    // Annotation: Test
    // Unit Test
    // Test that the media can be found by title
    public void testFindByTitle() {
        List<Media> mediaByTitle = mediaService.findMediaByTitle("Test");
        assertNotNull("The media list is void", mediaByTitle);
        assertFalse("The media list is void", mediaByTitle.isEmpty());
    }

    @Test
    // Annotation: Test
    // Unit Test
    // Test that the media can be found by author
    public void testFindByAuthor() {
        List<Book> booksByAuthor = mediaService.findBooksByAuthor("Test Author");
        assertNotNull("The book list is void", booksByAuthor);
        assertFalse("The book list is empty", booksByAuthor.isEmpty());
    }

    @Test
    // Annotation: Test
    // Unit Test
    // Test the media availability update
    public void testUpdate() throws LibraryException, MediaNotFoundException {
        testBook.setAvailable(false);
        Media updatedBook = mediaService.updateMedia(testBook);

        assertNotNull("The updated book is void", updatedBook);
        assertFalse("Availability status has not been updated", updatedBook.isAvailable());

        // Verify that the update is persistent
        Media retrievedBook = mediaService.findMediaById(testBook.getId());
        assertFalse("The availability status is not persistent", retrievedBook.isAvailable());
    }

    @Test(expected = MediaNotFoundException.class)
    // Annotation: Test
    // Unit Test
    // Test the media deletion
    public void testDelete() throws LibraryException, MediaNotFoundException {
        // Mock media to delete
        Media mediaToDelete = MediaFactory.createBook(
                "Delete Test Book",
                "Delete Test Author",
                LocalDate.now(),
                "Delete Test Publisher",
                200);

        mediaService.saveMedia(mediaToDelete);

        // Verify that the media is created
        Media foundMedia = mediaService.findMediaById(mediaToDelete.getId());
        assertNotNull("The media was not created", foundMedia);

        // Delete the media
        mediaService.deleteMedia(mediaToDelete.getId());

        // If the media does not exist, throw MediaNotFoundException
        mediaService.findMediaById(mediaToDelete.getId());
    }

    @Test
    // Annotation: Test
    // Unit Test
    // Test the creation of a collection and adding media to it
    public void testCollectionCreationAndAddMedia() throws LibraryException, MediaNotFoundException {
        // Create a new collection
        MediaCollection collection = MediaFactory.createMediaCollection("Test Collection");
        mediaService.saveMedia(collection);

        // Create a new book to add to the collection
        Media bookForCollection = MediaFactory.createBook(
                "Test Book for Collection",
                "Test Author Collection",
                LocalDate.now(),
                "Test Publisher Collection",
                200);
        mediaService.saveMedia(bookForCollection);

        // Add the book to the collection
        mediaService.addMediaToCollection(collection.getId(), bookForCollection.getId());

        // Verify the book was added to the collection
        Media updatedCollection = mediaService.findMediaById(collection.getId());
        assertTrue("The retrieved media is not a collection", updatedCollection instanceof MediaCollection);

        MediaCollection retrievedCollection = (MediaCollection) updatedCollection;
        assertFalse("The collection should not be empty", retrievedCollection.getMediaItems().isEmpty());
        assertEquals("The collection should contain 1 item", 1, retrievedCollection.getMediaItems().size());

        // Verify the book is not available
        Media retrievedBook = mediaService.findMediaById(bookForCollection.getId());
        assertFalse("The book should not be available when added to a collection", retrievedBook.isAvailable());
    }

    @Test
    // Annotation: Test
    // Unit Test
    // Test removing media from a collection
    public void testRemoveMediaFromCollection() throws LibraryException, MediaNotFoundException {
        // Create a new collection
        MediaCollection collection = MediaFactory.createMediaCollection("Test Collection for Removal");
        mediaService.saveMedia(collection);

        // Create a new book to add to the collection
        Media bookForCollection = MediaFactory.createBook(
                "Test Book for Removal",
                "Test Author Removal",
                LocalDate.now(),
                "Test Publisher Removal",
                200);
        mediaService.saveMedia(bookForCollection);

        // Add the book to the collection
        mediaService.addMediaToCollection(collection.getId(), bookForCollection.getId());

        // Remove the book from the collection
        mediaService.removeMediaFromCollection(collection.getId(), bookForCollection.getId());

        // Verify the book was removed from the collection
        Media updatedCollection = mediaService.findMediaById(collection.getId());
        MediaCollection retrievedCollection = (MediaCollection) updatedCollection;
        assertTrue("The collection should be empty after removal", retrievedCollection.getMediaItems().isEmpty());

        // Verify the book is now available
        Media retrievedBook = mediaService.findMediaById(bookForCollection.getId());
        assertTrue("The book should be available after being removed from a collection", retrievedBook.isAvailable());
    }

    @Test
    // Annotation: Test
    // Unit Test
    // Test finding media by publication year
    public void testFindByPublicationYear() {
        int currentYear = LocalDate.now().getYear();
        List<Media> mediaByYear = mediaService.findMediaByPublicationYear(currentYear);
        assertNotNull("The media list should not be null", mediaByYear);
        assertFalse("The media list should not be empty", mediaByYear.isEmpty());

        // Verify that all media in the list have the correct publication year
        for (Media media : mediaByYear) {
            assertEquals("Publication year should match the search criteria",
                    currentYear, media.getPublicationDate().getYear());
        }
    }
}