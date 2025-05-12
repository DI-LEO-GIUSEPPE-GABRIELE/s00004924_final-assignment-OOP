package service;

import exception.LibraryException;
import exception.MediaNotFoundException;
import factory.MediaFactory;
import model.media.Book;
import model.media.Media;
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
    // Test that all media can be found
    public void testFindAll() throws LibraryException {
        List<Media> allMedia = mediaService.findAllMedia();
        assertNotNull("The media list is void", allMedia);
        assertFalse("The media list is void", allMedia.isEmpty());
    }

    @Test
    // Test that the media can be found by title
    public void testFindByTitle() {
        List<Media> mediaByTitle = mediaService.findMediaByTitle("Test");
        assertNotNull("The media list is void", mediaByTitle);
        assertFalse("The media list is void", mediaByTitle.isEmpty());
    }

    @Test
    // Test that the media can be found by author
    public void testFindByAuthor() {
        List<Book> booksByAuthor = mediaService.findBooksByAuthor("Test Author");
        assertNotNull("The book list is void", booksByAuthor);
        assertFalse("The book list is empty", booksByAuthor.isEmpty());
    }

    @Test
    // Test the media update
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
}