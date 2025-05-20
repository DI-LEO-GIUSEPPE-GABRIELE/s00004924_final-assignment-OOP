package service;

import exception.LibraryException;
import exception.MediaNotFoundException;
import factory.MediaFactory;
import model.media.Media;
import model.media.MediaCollection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.doReturn;
import org.mockito.junit.MockitoJUnitRunner;
import repository.MediaRepository;
import util.LoggerManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

// Class for MediaService tests with Mockito integration
@RunWith(MockitoJUnitRunner.class)
public class MediaServiceJUnitTest {
    private static final Logger LOGGER = LoggerManager.getLogger(MediaServiceJUnitTest.class.getName());

    @Mock
    // Annotation: Mock creation for the MediaRepository
    private MediaRepository mediaRepository;

    @InjectMocks
    // Annotation: Inject the mock into the MediaService
    private MediaService mediaService;

    @Spy
    // Annotation: Create a spy of the MediaService
    private MediaService mediaServiceSpy;

    private Media testBook;
    private Media testMagazine;

    @Before
    // Annotation: Execute before each test
    public void setUp() throws LibraryException {
        // Initialization of service and mocks
        LOGGER.info("Test initialization");
        mediaServiceSpy = Mockito.spy(MediaService.getInstance());

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

        // Configuration of mocks for saving and finding media
        when(mediaRepository.save(any(Media.class))).thenReturn(testBook);
        when(mediaRepository.findById(testBook.getId())).thenReturn(testBook);
        when(mediaRepository.findById(testMagazine.getId())).thenReturn(testMagazine);

        // Saving media
        mediaService.saveMedia(testBook);
        mediaService.saveMedia(testMagazine);

        LOGGER.info("Setup completed successfully");
    }

    // UNIT TESTS

    @Test
    // Unit Test: Verify that the media is saved and can be found by ID
    public void testSaveAndFindById() throws LibraryException, MediaNotFoundException {
        LOGGER.info("Unit test: Save and find by ID");

        Media foundBook = mediaService.findMediaById(testBook.getId());
        Media foundMagazine = mediaService.findMediaById(testMagazine.getId());

        assertNotNull("Book not found", foundBook);
        assertNotNull("Magazine not found", foundMagazine);
        assertEquals("Book ID does not match", testBook.getId(), foundBook.getId());
        assertEquals("Magazine ID does not match", testMagazine.getId(), foundMagazine.getId());
    }

    @Test
    // Unit Test: Verify that all media can be found using Mockito
    public void testFindAll() throws LibraryException {
        LOGGER.info("Unit test: Find all media");

        // Preparation of test data
        List<Media> mockMediaList = new ArrayList<>();
        mockMediaList.add(testBook);
        mockMediaList.add(testMagazine);

        // Configuration of mock behavior
        when(mediaRepository.findAll()).thenReturn(mockMediaList);

        List<Media> allMedia = mediaService.findAllMedia();

        // Verification of results
        assertNotNull("The media list is null", allMedia);
        assertFalse("The media list is empty", allMedia.isEmpty());
        assertEquals("The size of the media list does not match", 2, allMedia.size());

        // Verify that the repository's findAll method is called
        verify(mediaRepository).findAll();
    }

    @Test
    // Unit Test: Verify that the media can be found by title using Mockito
    public void testFindByTitle() {
        LOGGER.info("Unit test: Find by title");

        // Preparation of test data
        List<Media> mockMediaList = new ArrayList<>();
        mockMediaList.add(testBook);
        mockMediaList.add(testMagazine);

        // Configuration of mock behavior
        when(mediaRepository.findByTitle(anyString())).thenReturn(mockMediaList);

        List<Media> mediaByTitle = mediaService.findMediaByTitle("Test");

        // Verification of results
        assertNotNull("The media list is null", mediaByTitle);
        assertFalse("The media list is empty", mediaByTitle.isEmpty());

        // Verify that the repository's findByTitle method was called with the correct
        // parameter
        verify(mediaRepository).findByTitle("Test");
    }

    @Test
    // Unit Test: Verify the media availability update with Mockito
    public void testUpdate() throws LibraryException, MediaNotFoundException {
        LOGGER.info("Unit test: Media update");

        // Preparation of test with Mockito
        testBook.setAvailable(false);
        when(mediaRepository.update(testBook)).thenReturn(testBook);

        // Using spy to verify internal behavior
        doReturn(testBook).when(mediaServiceSpy).findMediaById(testBook.getId());

        Media updatedBook = mediaService.updateMedia(testBook);

        assertNotNull("The updated book is null", updatedBook);
        assertFalse("The availability status has not been updated", updatedBook.isAvailable());

        // Verify that the update is persistent
        Media retrievedBook = mediaService.findMediaById(testBook.getId());
        assertFalse("The availability status is not persistent", retrievedBook.isAvailable());

        // Verify that the repository's update method was called
        verify(mediaRepository).update(testBook);
    }

    @Test(expected = MediaNotFoundException.class)
    // Unit Test: Verify the media deletion with Mockito verification
    public void testDelete() throws LibraryException, MediaNotFoundException {
        LOGGER.info("Unit test: Media deletion");

        // Mock of the media to delete
        Media mediaToDelete = MediaFactory.createBook(
                "Delete Test Book",
                "Delete Test Author",
                LocalDate.now(),
                "Delete Test Publisher",
                200);

        // Configuration of mock behavior for the media to delete
        when(mediaRepository.findById(mediaToDelete.getId()))
                .thenReturn(mediaToDelete)
                .thenThrow(new MediaNotFoundException("Media not found"));
        doNothing().when(mediaRepository).delete(mediaToDelete.getId());

        mediaService.saveMedia(mediaToDelete);

        // Verify that the media is created
        Media foundMedia = mediaService.findMediaById(mediaToDelete.getId());
        assertNotNull("The media was not created", foundMedia);

        // Delete the media
        mediaService.deleteMedia(mediaToDelete.getId());

        // Verify that the delete method is called with the media ID
        verify(mediaRepository).delete(mediaToDelete.getId());

        // If the media doesn't exist, throws MediaNotFoundException
        mediaService.findMediaById(mediaToDelete.getId());
    }

    @Test
    // Unit Test: Verify the collection creation and add media to it
    public void testCollectionCreationAndAddMedia() throws LibraryException, MediaNotFoundException {
        LOGGER.info("Unit test: Collection creation and media addition");

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

        // Configuration of mock behavior for the collection
        MediaCollection updatedCollection = (MediaCollection) collection;
        updatedCollection.addMedia(bookForCollection);
        when(mediaRepository.findById(collection.getId())).thenReturn(updatedCollection);

        // Add the book to the collection
        mediaService.addMediaToCollection(collection.getId(), bookForCollection.getId());

        // Verify that the book is added to the collection
        Media retrievedCollection = mediaService.findMediaById(collection.getId());
        assertTrue("Il media recuperato non è una collezione", retrievedCollection instanceof MediaCollection);

        MediaCollection collectionResult = (MediaCollection) retrievedCollection;
        assertFalse("The collection should not be empty", collectionResult.getMediaItems().isEmpty());
        assertEquals("The collection should contain at least one item", 1, collectionResult.getMediaItems().size());

        // Configuration of mock behavior for the updated book (not available)
        bookForCollection.setAvailable(false);
        when(mediaRepository.findById(bookForCollection.getId())).thenReturn(bookForCollection);

        // Verify that the book is not available
        Media retrievedBook = mediaService.findMediaById(bookForCollection.getId());
        assertFalse("The book should not be available when added to a collection",
                retrievedBook.isAvailable());
    }

    @Test
    // Unit Test: Verify the media removal from a collection
    public void testRemoveMediaFromCollection() throws LibraryException, MediaNotFoundException {
        LOGGER.info("Unit test: Media removal from collection");

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
        MediaCollection collectionWithBook = (MediaCollection) collection;
        collectionWithBook.addMedia(bookForCollection);
        when(mediaRepository.findById(collection.getId())).thenReturn(collectionWithBook);

        mediaService.addMediaToCollection(collection.getId(), bookForCollection.getId());

        // Remove the book from the collection
        MediaCollection emptyCollection = (MediaCollection) collection;
        emptyCollection.getMediaItems().clear();
        when(mediaRepository.findById(collection.getId())).thenReturn(emptyCollection);

        mediaService.removeMediaFromCollection(collection.getId(), bookForCollection.getId());

        // Verify that the book is removed from the collection
        Media updatedCollection = mediaService.findMediaById(collection.getId());
        MediaCollection retrievedCollection = (MediaCollection) updatedCollection;
        assertTrue("The collection should be empty after removal",
                retrievedCollection.getMediaItems().isEmpty());

        // Configure the mock for the updated book (available)
        bookForCollection.setAvailable(true);
        when(mediaRepository.findById(bookForCollection.getId())).thenReturn(bookForCollection);

        // Verify that the book is now available
        Media retrievedBook = mediaService.findMediaById(bookForCollection.getId());
        assertTrue("The book should be available after being removed from a collection",
                retrievedBook.isAvailable());
    }

    @Test
    // Unit Test: Verify the media search by publication year
    public void testFindByPublicationYear() {
        LOGGER.info("Unit test: Find by publication year");

        int currentYear = LocalDate.now().getYear();

        // Configuration of mock for year search
        List<Media> yearResults = new ArrayList<>();
        yearResults.add(testBook);
        yearResults.add(testMagazine);
        when(mediaRepository.findByPublicationYear(currentYear)).thenReturn(yearResults);

        List<Media> mediaByYear = mediaService.findMediaByPublicationYear(currentYear);

        assertNotNull("The media list should not be null", mediaByYear);
        assertFalse("The media list should not be empty", mediaByYear.isEmpty());

        // Verify that the repository's findByPublicationYear method is called
        verify(mediaRepository).findByPublicationYear(currentYear);

        assertTrue("The media returned should be the ones configured in the mock",
                mediaByYear.containsAll(yearResults) && yearResults.containsAll(mediaByYear));
    }

    // INTEGRATION TESTS

    @Test
    // Integration Test: Creation and saving with repository verification
    public void testCreateAndSaveIntegration() throws LibraryException {
        LOGGER.info("Integration test: Creation and saving");

        // Configuration of mock behavior for saving
        doAnswer(invocation -> invocation.getArgument(0)).when(mediaRepository).save(any(Media.class));

        Media book = MediaFactory.createBook(
                "Il Nome della Rosa",
                "Umberto Eco",
                LocalDate.of(1980, 10, 15),
                "Bompiani",
                512);

        Media savedBook = mediaService.saveMedia(book);
        LOGGER.info("Book created and saved: " + book.getDetails());

        // Verify that the book is saved successfully
        assertNotNull("The book should be saved successfully", savedBook);

        Media magazine = MediaFactory.createMagazine(
                "Focus",
                LocalDate.of(2023, 5, 10),
                "Mondadori",
                350);

        Media savedMagazine = mediaService.saveMedia(magazine);
        LOGGER.info("Magazine created and saved: " + magazine.getDetails());

        // Verify that the magazine is saved successfully
        assertNotNull("The magazine should be saved successfully", savedMagazine);

        // Verify that the save method was called twice
        verify(mediaRepository, times(2 + 2)).save(any(Media.class));

        // Configuration of mock to simulate search for all media
        List<Media> mockMediaList = new ArrayList<>();
        mockMediaList.add(book);
        mockMediaList.add(magazine);
        mockMediaList.add(testBook);
        mockMediaList.add(testMagazine);
        when(mediaRepository.findAll()).thenReturn(mockMediaList);

        List<Media> allMedia = mediaService.findAllMedia();
        LOGGER.info("Number of saved media: " + allMedia.size());

        // Verify that the correct number of media was returned
        assertNotNull("The media list should not be null", allMedia);
        assertEquals("There should be 4 saved media", 4, allMedia.size());
        assertTrue("The media list should contain the book", allMedia.contains(book));
        assertTrue("The media list should contain the magazine", allMedia.contains(magazine));
    }

    @Test
    // Integration Test: Search with different methods
    public void testSearchIntegration() throws LibraryException {
        LOGGER.info("Integration test: Search with different methods");

        // Creation of test data
        Media book = MediaFactory.createBook(
                "Il Nome della Rosa",
                "Umberto Eco",
                LocalDate.of(1980, 10, 15),
                "Bompiani",
                512);

        // Configuration of mocks for different searches
        List<Media> titleResults = new ArrayList<>();
        titleResults.add(book);
        when(mediaRepository.findByTitle("Rosa")).thenReturn(titleResults);

        List<Media> yearResults = new ArrayList<>();
        yearResults.add(book);
        // Configure the mock to return the year results
        when(mediaRepository.findByPublicationYear(1980)).thenReturn(yearResults);

        // Search by title
        List<Media> mediaByTitle = mediaService.findMediaByTitle("Rosa");
        LOGGER.info("Media found for title 'Rosa': " + mediaByTitle.size());
        assertEquals("Should find one media with title 'Rosa'", 1, mediaByTitle.size());

        // Search by year
        List<Media> mediaByYear = mediaService.findMediaByPublicationYear(1980);
        LOGGER.info("Media found for year 1980: " + mediaByYear.size());
        assertEquals("Should find one media with publication year 1980", 1, mediaByYear.size());

        // Verification of results
        assertTrue("The title search results should contain the expected book", mediaByTitle.contains(book));
        assertTrue("The year search results should contain the expected book", mediaByYear.contains(book));

        // Verify that the repository methods were called with the correct parameters
        verify(mediaRepository).findByTitle("Rosa");
        verify(mediaRepository).findByPublicationYear(1980);
    }

    @Test
    // Integration Test: Availability update
    public void testUpdateIntegration() throws LibraryException, MediaNotFoundException {
        LOGGER.info("Integration test: Availability update");

        // Configuration of mock for title search
        List<Media> books = new ArrayList<>();
        books.add(testBook);
        when(mediaRepository.findByTitle("Test Book")).thenReturn(books);

        // Configuration of mock for update
        testBook.setAvailable(false);
        when(mediaRepository.update(testBook)).thenReturn(testBook);

        // Search for a book to update
        List<Media> foundBooks = mediaService.findMediaByTitle("Test Book");
        assertFalse("The book list should not be empty", foundBooks.isEmpty());

        Media book = foundBooks.get(0);
        book.setAvailable(false);
        mediaService.updateMedia(book);
        LOGGER.info("Book updated: " + book.getDetails());

        Media updatedBook = mediaService.findMediaById(book.getId());
        assertFalse("The availability status has not been updated", updatedBook.isAvailable());

        LOGGER.info("Update verification completed successfully");
    }

    @Test
    // Integration Test: Deletion with repository verification
    public void testDeleteIntegration() throws LibraryException, MediaNotFoundException {
        LOGGER.info("Integration test: Deletion");

        Media mediaToDelete = MediaFactory.createBook(
                "Book to delete",
                "Author Test",
                LocalDate.now(),
                "Publisher Test",
                100);

        // Configuration of mock to simulate saving and deletion
        when(mediaRepository.save(mediaToDelete)).thenReturn(mediaToDelete);
        doNothing().when(mediaRepository).delete(mediaToDelete.getId());

        // Configuration of mock to simulate search before and after deletion
        when(mediaRepository.findById(mediaToDelete.getId()))
                .thenReturn(mediaToDelete)
                .thenThrow(new MediaNotFoundException("Media not found"));

        mediaService.saveMedia(mediaToDelete);
        LOGGER.info("Media created for deletion test: " + mediaToDelete.getDetails());
        verify(mediaRepository).save(mediaToDelete);

        mediaService.deleteMedia(mediaToDelete.getId());
        LOGGER.info("Media deleted");
        verify(mediaRepository).delete(mediaToDelete.getId());

        try {
            mediaService.findMediaById(mediaToDelete.getId());
            fail("A MediaNotFoundException should be thrown");
        } catch (MediaNotFoundException e) {
            LOGGER.info("Deletion verification completed successfully");
            // Verify that findById was called twice
            verify(mediaRepository, times(2)).findById(mediaToDelete.getId());
        }
    }

    @Test
    // Integration Test: Collection management
    public void testCollectionsIntegration() throws LibraryException, MediaNotFoundException {
        LOGGER.info("Integration test: Collection management");

        // Creation of a new collection
        MediaCollection collection = MediaFactory.createMediaCollection("Test Collection Integration");
        mediaService.saveMedia(collection);
        LOGGER.info("Collection created: " + collection.getDetails());

        Media bookForCollection = MediaFactory.createBook(
                "Test book for collection integration",
                "Test Author Collection Integration",
                LocalDate.now(),
                "Test Publisher Collection Integration",
                200);

        mediaService.saveMedia(bookForCollection);
        LOGGER.info("Book created for the collection: " + bookForCollection.getDetails());

        Media secondBookForCollection = MediaFactory.createBook(
                "Pirates of the Caribbean",
                "Menichella",
                LocalDate.of(2007, 5, 24),
                "Disney Books",
                523);

        mediaService.saveMedia(secondBookForCollection);
        LOGGER.info("Second book created for the collection: " + secondBookForCollection.getDetails());

        // Configuration of mocks for collection management
        MediaCollection updatedCollection = (MediaCollection) collection;
        updatedCollection.addMedia(bookForCollection);
        updatedCollection.addMedia(secondBookForCollection);
        when(mediaRepository.findById(collection.getId())).thenReturn(updatedCollection);

        // Configuration of mocks for books (not available when added to
        // collection)
        bookForCollection.setAvailable(false);
        secondBookForCollection.setAvailable(false);
        when(mediaRepository.findById(bookForCollection.getId())).thenReturn(bookForCollection);
        when(mediaRepository.findById(secondBookForCollection.getId())).thenReturn(secondBookForCollection);

        // Add books to the collection
        mediaService.addMediaToCollection(collection.getId(), bookForCollection.getId());
        LOGGER.info("First book added to the collection");

        mediaService.addMediaToCollection(collection.getId(), secondBookForCollection.getId());
        LOGGER.info("Second book added to the collection");

        // Verify that books have been added to the collection
        Media retrievedCollection = mediaService.findMediaById(collection.getId());
        assertTrue("The retrieved media is not a collection", retrievedCollection instanceof MediaCollection);

        MediaCollection collectionResult = (MediaCollection) retrievedCollection;
        assertEquals("The collection should contain two items", 2, collectionResult.getMediaItems().size());

        // Verify that books are not available
        Media retrievedBook = mediaService.findMediaById(bookForCollection.getId());
        assertFalse("The book should not be available when added to a collection",
                retrievedBook.isAvailable());

        // Configuration for removing a book from the collection
        MediaCollection collectionAfterRemoval = (MediaCollection) collection;
        collectionAfterRemoval.getMediaItems().remove(bookForCollection);
        when(mediaRepository.findById(collection.getId())).thenReturn(collectionAfterRemoval);

        // Configuration of the removed book (now available)
        bookForCollection.setAvailable(true);
        when(mediaRepository.findById(bookForCollection.getId())).thenReturn(bookForCollection);

        // Test removing a book from the collection
        mediaService.removeMediaFromCollection(collection.getId(), bookForCollection.getId());
        LOGGER.info("Book removed from the collection");

        // Verify that the book has been removed
        retrievedCollection = mediaService.findMediaById(collection.getId());
        collectionResult = (MediaCollection) retrievedCollection;
        assertEquals("The collection should contain 1 item after removal", 1,
                collectionResult.getMediaItems().size());

        // Verify that the book is now available
        retrievedBook = mediaService.findMediaById(bookForCollection.getId());
        assertTrue("The book should be available after being removed from a collection",
                retrievedBook.isAvailable());

        LOGGER.info("Collection verification completed successfully");
    }

    @Test
    // End-to-End Test: Complete workflow from creation to search and update
    public void testEndToEndWorkflow() throws LibraryException, MediaNotFoundException {
        LOGGER.info("End-to-End test: Complete workflow");

        // Create different types of media
        Media book = MediaFactory.createBook(
                "The Lord of the Rings",
                "J.R.R. Tolkien",
                LocalDate.of(1954, 7, 29),
                "Allen & Unwin",
                423);

        Media magazine = MediaFactory.createMagazine(
                "National Geographic",
                LocalDate.of(2023, 1, 15),
                "National Geographic Society",
                120);

        MediaCollection collection = MediaFactory.createMediaCollection("Fantasy Collection");

        // Configure mocks for saving
        when(mediaRepository.save(any(Media.class))).thenAnswer(i -> i.getArgument(0));

        // Save all media
        mediaService.saveMedia(book);
        mediaService.saveMedia(magazine);
        mediaService.saveMedia(collection);
        LOGGER.info("All media created and saved");

        // Configure mocks for finding by ID
        when(mediaRepository.findById(book.getId())).thenReturn(book);
        when(mediaRepository.findById(magazine.getId())).thenReturn(magazine);
        when(mediaRepository.findById(collection.getId())).thenReturn(collection);

        // Search media by different criteria
        // Configure mock for title search
        List<Media> lordResults = new ArrayList<>();
        lordResults.add(book);
        when(mediaRepository.findByTitle("Lord")).thenReturn(lordResults);

        // Configure mock for year search
        List<Media> year1954Results = new ArrayList<>();
        year1954Results.add(book);
        when(mediaRepository.findByPublicationYear(1954)).thenReturn(year1954Results);

        List<Media> titleResults = mediaService.findMediaByTitle("Lord");
        List<Media> yearResults = mediaService.findMediaByPublicationYear(1954);

        // Verify search results
        assertEquals("Should find one book by title", 1, titleResults.size());
        assertEquals("Should find one book by year", 1, yearResults.size());
        LOGGER.info("All search operations completed successfully");

        // Add book to collection
        // Configure collection after adding book
        MediaCollection updatedCollection = (MediaCollection) collection;
        updatedCollection.addMedia(book);
        when(mediaRepository.findById(collection.getId())).thenReturn(updatedCollection);

        // Book becomes unavailable when added to collection
        book.setAvailable(false);
        when(mediaRepository.findById(book.getId())).thenReturn(book);

        // Add book to collection
        mediaService.addMediaToCollection(collection.getId(), book.getId());

        // Verify book is in collection and unavailable
        Media retrievedCollection = mediaService.findMediaById(collection.getId());
        MediaCollection collectionResult = (MediaCollection) retrievedCollection;
        assertEquals("Collection should contain 1 item", 1, collectionResult.getMediaItems().size());

        Media retrievedBook = mediaService.findMediaById(book.getId());
        assertFalse("Book should be unavailable", retrievedBook.isAvailable());
        LOGGER.info("Book added to collection successfully");

        // Update magazine availability
        magazine.setAvailable(false);
        when(mediaRepository.update(magazine)).thenReturn(magazine);

        Media updatedMagazine = mediaService.updateMedia(magazine);
        assertFalse("Magazine should be unavailable after update", updatedMagazine.isAvailable());
        LOGGER.info("Magazine availability updated successfully");

        // Remove book from collection
        // Configure collection after removal
        MediaCollection collectionAfterRemoval = (MediaCollection) collection;
        collectionAfterRemoval.getMediaItems().clear();
        when(mediaRepository.findById(collection.getId())).thenReturn(collectionAfterRemoval);

        // Book becomes available when removed from collection
        book.setAvailable(true);
        when(mediaRepository.findById(book.getId())).thenReturn(book);

        // Remove book from collection
        mediaService.removeMediaFromCollection(collection.getId(), book.getId());

        // Verify book is removed and available
        retrievedCollection = mediaService.findMediaById(collection.getId());
        collectionResult = (MediaCollection) retrievedCollection;
        assertTrue("Collection should be empty", collectionResult.getMediaItems().isEmpty());

        retrievedBook = mediaService.findMediaById(book.getId());
        assertTrue("Book should be available after removal", retrievedBook.isAvailable());
        LOGGER.info("Book removed from collection successfully");

        // Delete magazine
        doNothing().when(mediaRepository).delete(magazine.getId());
        when(mediaRepository.findById(magazine.getId()))
                .thenReturn(magazine)
                .thenThrow(new MediaNotFoundException("Media not found"));

        mediaService.deleteMedia(magazine.getId());

        try {
            mediaService.findMediaById(magazine.getId());
            fail("Should throw MediaNotFoundException");
        } catch (MediaNotFoundException e) {
            // Expected exception
            LOGGER.info("Magazine deleted successfully");
        }

        LOGGER.info("End-to-End workflow completed successfully");
    }
}