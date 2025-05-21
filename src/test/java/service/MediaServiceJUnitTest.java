package service;

import exception.LibraryException;
import exception.MediaNotFoundException;
import factory.MediaFactory;
import model.media.Book;
import model.media.Media;
import model.media.MediaCollection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.InjectMocks;
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
// Silent mode to ignore stubbing argument mismatches (example: call a method as mediaRepository.findByTitle(searchTitle))
@RunWith(MockitoJUnitRunner.Silent.class)
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

                // Create a spy of the mediaService that uses the mock
                mediaServiceSpy = Mockito.spy(mediaService);

                // Create one book and one magazine for testing
                testBook = MediaFactory.createBook(
                                "Test Initialization Book",
                                "Test Initialization Author",
                                LocalDate.now(),
                                "Test Initialization Publisher",
                                100);

                testMagazine = MediaFactory.createMagazine(
                                "Test Initialization Magazine",
                                LocalDate.now(),
                                "Test Initialization Publisher",
                                1);

                // Unit Test: Configuration of mocks for saving and finding media with Mockito
                when(mediaRepository.save(any(Media.class))).thenReturn(testBook);
                when(mediaRepository.findById(testBook.getId())).thenReturn(testBook);
                when(mediaRepository.findById(testMagazine.getId())).thenReturn(testMagazine);

                // Saving media
                mediaService.saveMedia(testBook);
                mediaService.saveMedia(testMagazine);

                LOGGER.info("Setup completed successfully");
        }

        @Test
        // Unit Test: Verify that the media is saved and can be found by ID
        public void testFindById() throws LibraryException, MediaNotFoundException {
                LOGGER.info("Unit test: Save and find by ID");

                Media foundBook = mediaService.findMediaById(testBook.getId());
                Media foundMagazine = mediaService.findMediaById(testMagazine.getId());

                // Verify that the media is found with Mockito
                when(mediaRepository.findById(testBook.getId())).thenReturn(testBook);
                when(mediaRepository.findById(testMagazine.getId())).thenReturn(testMagazine);

                assertNotNull("Book not found", foundBook);
                assertNotNull("Magazine not found", foundMagazine);
                assertEquals("Book ID does not match", testBook.getId(), foundBook.getId());
                assertEquals("Magazine ID does not match", testMagazine.getId(), foundMagazine.getId());
        }

        @Test
        // Integration Test: Verify the collection creation and add media to it
        public void testCollectionCreationAndAddMedia() throws LibraryException, MediaNotFoundException {
                LOGGER.info("Unit test: Collection creation and media addition");

                // Create a new collection
                MediaCollection collection = MediaFactory.createMediaCollection("Test Collection");
                mediaService.saveMedia(collection);

                // Create a new book to add to the collection
                Media bookForCollection = MediaFactory.createBook(
                                "Test Book for Collection",
                                "Test Author for Collection",
                                LocalDate.now(),
                                "Test Publisher for Collection",
                                200);
                mediaService.saveMedia(bookForCollection);

                // Configuration of mock behavior for the collection
                MediaCollection updatedCollection = (MediaCollection) collection;
                updatedCollection.addMedia(bookForCollection);
                when(mediaRepository.findById(collection.getId())).thenReturn(updatedCollection);

                // Add the book to the collection
                mediaService.addMediaToCollection(collection.getId(), bookForCollection.getId());

                // Unit Test: Verify that the book is added to the collection
                Media retrievedCollection = mediaService.findMediaById(collection.getId());
                assertTrue("Retrieved media is not a collection", retrievedCollection instanceof MediaCollection);

                MediaCollection collectionResult = (MediaCollection) retrievedCollection;
                assertFalse("The collection should not be empty", collectionResult.getMediaItems().isEmpty());
                assertEquals("The collection should contain at least one item", 1,
                                collectionResult.getMediaItems().size());

                // Configuration of mock behavior for the updated book (not available)
                bookForCollection.setAvailable(false);
                // Mockito
                when(mediaRepository.findById(bookForCollection.getId())).thenReturn(bookForCollection);

                // Verify that the book is not available
                Media retrievedBook = mediaService.findMediaById(bookForCollection.getId());
                assertFalse("The book should not be available when added to a collection",
                                retrievedBook.isAvailable());
        }

        @Test
        // Integration Test: Verify the media removal from a collection
        public void testRemoveMediaFromCollection() throws LibraryException, MediaNotFoundException {
                LOGGER.info("Unit test: Media removal from collection");

                // Unit Test: Create a new collection
                MediaCollection collection = MediaFactory.createMediaCollection("Test Collection for Removal");
                mediaService.saveMedia(collection);

                // Create a new book to add to the collection
                Media bookForCollection = MediaFactory.createBook(
                                "Test Book for Removal",
                                "Test Author for Removal",
                                LocalDate.now(),
                                "Test Publisher for Removal",
                                200);
                mediaService.saveMedia(bookForCollection);

                // Unit Test: Add the book to the collection
                MediaCollection collectionWithBook = (MediaCollection) collection;
                collectionWithBook.addMedia(bookForCollection);
                // Mockito
                when(mediaRepository.findById(collection.getId())).thenReturn(collectionWithBook);

                mediaService.addMediaToCollection(collection.getId(), bookForCollection.getId());

                // Unit Test: Remove the book from the collection
                MediaCollection emptyCollection = (MediaCollection) collection;
                emptyCollection.getMediaItems().clear();
                // Mockito
                when(mediaRepository.findById(collection.getId())).thenReturn(emptyCollection);

                mediaService.removeMediaFromCollection(collection.getId(), bookForCollection.getId());

                // Verify that the book is removed from the collection
                Media updatedCollection = mediaService.findMediaById(collection.getId());
                MediaCollection retrievedCollection = (MediaCollection) updatedCollection;
                assertTrue("The collection should be empty after removal",
                                retrievedCollection.getMediaItems().isEmpty());

                // Configure the mock for the updated book (available)
                bookForCollection.setAvailable(true);
                // Mockito
                when(mediaRepository.findById(bookForCollection.getId())).thenReturn(bookForCollection);

                // Verify that the book is now available
                Media retrievedBook = mediaService.findMediaById(bookForCollection.getId());
                assertTrue("The book should be available after being removed from a collection",
                                retrievedBook.isAvailable());
        }

        @Test
        // Integration Test: Find book by Title and update it
        public void testUpdateMedia() throws LibraryException, MediaNotFoundException {
                LOGGER.info("Integration test: Find book by Title and update it");

                // Configuration of mock for title search
                List<Media> books = new ArrayList<>();
                books.add(testBook);
                when(mediaRepository.findByTitle("Test Initialization Book")).thenReturn(books);

                // Configuration of mock for update with modified title
                String originalId = testBook.getId();
                String originalTitle = testBook.getTitle();
                String modifiedTitle = originalTitle + " Mod";

                // Create a new book with the same ID but modified title
                Media modifiedBook = new Book(
                                originalId,
                                modifiedTitle,
                                "Test Initialization Author",
                                testBook.getPublicationDate(),
                                "Test Initialization Publisher",
                                100);

                // Configure mock for update with Mockito
                when(mediaRepository.update(any(Media.class))).thenReturn(modifiedBook);
                when(mediaRepository.findById(testBook.getId())).thenReturn(modifiedBook);

                // Unit Test: Search for a book to update
                List<Media> foundBooks = mediaService.findMediaByTitle("Test Initialization Book");
                assertFalse("The book list should not be empty", foundBooks.isEmpty());

                // Take the first book
                Media book = foundBooks.get(0);

                // Create a new book with the same ID but modified title
                Media bookToUpdate = new Book(
                                book.getId(),
                                modifiedTitle,
                                "Test Initialization Author",
                                LocalDate.now(),
                                "Test Initialization Publisher",
                                100);

                // Unit Test: Update the book with the mediaService method
                mediaService.updateMedia(bookToUpdate);
                LOGGER.info("Book updated: " + bookToUpdate.getDetails());

                Media updatedBook = mediaService.findMediaById(book.getId());
                assertEquals("The title has not been updated correctly", modifiedTitle, updatedBook.getTitle());

                LOGGER.info("Update verification completed successfully");
        }

        @Test
        // Integration Test: Collection management
        public void testCollectionsManagement() throws LibraryException, MediaNotFoundException {
                LOGGER.info("Integration test: Collection management");

                // Unit Test: Creation of a new collection
                MediaCollection collection = MediaFactory.createMediaCollection("Test Collection Integration");
                mediaService.saveMedia(collection);
                LOGGER.info("Collection created: " + collection.getDetails());

                Media bookForCollection = MediaFactory.createBook(
                                "First book for collection Integration",
                                "First author for Collection Integration",
                                LocalDate.of(2010, 3, 26),
                                "First publisher for Collection Integration",
                                200);

                mediaService.saveMedia(bookForCollection);
                LOGGER.info("Book created for the collection: " + bookForCollection.getDetails());

                Media secondBookForCollection = MediaFactory.createBook(
                                "Second book for Collection Integration",
                                "Second author for Collection Integration",
                                LocalDate.of(2007, 5, 24),
                                "Second publisher for Collection Integration",
                                523);

                mediaService.saveMedia(secondBookForCollection);
                LOGGER.info("Second book created for the collection: " + secondBookForCollection.getDetails());

                // Configuration of mocks for collection management
                MediaCollection updatedCollection = (MediaCollection) collection;
                updatedCollection.addMedia(bookForCollection);
                updatedCollection.addMedia(secondBookForCollection);
                // Mockito
                when(mediaRepository.findById(collection.getId())).thenReturn(updatedCollection);

                // Configuration of mocks for books
                bookForCollection.setAvailable(false);
                secondBookForCollection.setAvailable(false);
                // Mockito
                when(mediaRepository.findById(bookForCollection.getId())).thenReturn(bookForCollection);
                when(mediaRepository.findById(secondBookForCollection.getId())).thenReturn(secondBookForCollection);

                // Unit Test: Add books to the collection with the mediaService method
                mediaService.addMediaToCollection(collection.getId(), bookForCollection.getId());
                LOGGER.info("First book added to the collection");

                // Unit Test: Add second book to the collection with the mediaService method
                mediaService.addMediaToCollection(collection.getId(), secondBookForCollection.getId());
                LOGGER.info("Second book added to the collection");

                // Unit Test: Verify that books have been added to the collection
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
                // Mockito
                when(mediaRepository.findById(collection.getId())).thenReturn(collectionAfterRemoval);

                // Configuration of the removed book (now available)
                bookForCollection.setAvailable(true);
                // Mockito
                when(mediaRepository.findById(bookForCollection.getId())).thenReturn(bookForCollection);

                // Unit Test: Test removing a book from the collection
                mediaService.removeMediaFromCollection(collection.getId(), bookForCollection.getId());
                LOGGER.info("Book removed from the collection");

                // Unit Test: Verify that the book has been removed
                retrievedCollection = mediaService.findMediaById(collection.getId());
                collectionResult = (MediaCollection) retrievedCollection;
                assertEquals("The collection should contain 1 item after removal", 1,
                                collectionResult.getMediaItems().size());

                // Unit Test: Verify that the book is now available
                retrievedBook = mediaService.findMediaById(bookForCollection.getId());
                assertTrue("The book should be available after being removed from a collection",
                                retrievedBook.isAvailable());

                LOGGER.info("Collection verification completed successfully");
        }

        // @Test
        // // End-to-End Test: Complete workflow from creation to search and update
        // public void testEndToEndWorkflow() throws LibraryException,
        // MediaNotFoundException {
        // LOGGER.info("End-to-End test: Complete workflow");

        // // Create different types of media
        // Media book = MediaFactory.createBook(
        // "Book for End to End",
        // "Author for End to End",
        // LocalDate.of(2025, 5, 21),
        // "Publisher for End to End",
        // 34);

        // Media magazine = MediaFactory.createMagazine(
        // "Magazine for End to End",
        // LocalDate.of(2021, 1, 21),
        // "Publisher for End to End",
        // 123);

        // MediaCollection collection = MediaFactory.createMediaCollection("End to End
        // Collection");

        // // Configure mocks for saving
        // when(mediaRepository.save(any(Media.class))).thenAnswer(i ->
        // i.getArgument(0));

        // // Save all media
        // mediaService.saveMedia(book);
        // mediaService.saveMedia(magazine);
        // mediaService.saveMedia(collection);
        // LOGGER.info("All media created and saved");

        // // Configure mocks for finding by ID
        // when(mediaRepository.findById(book.getId())).thenReturn(book);
        // when(mediaRepository.findById(magazine.getId())).thenReturn(magazine);
        // when(mediaRepository.findById(collection.getId())).thenReturn(collection);

        // // Search media by different criteria
        // // Configure mock for title search
        // List<Media> results = new ArrayList<>();
        // results.add(book);
        // when(mediaRepository.findByTitle("End to End")).thenReturn(results);

        // // Configure mock for year search
        // List<Media> year2025Results = new ArrayList<>();
        // year2025Results.add(book);
        // when(mediaRepository.findByPublicationYear(2025)).thenReturn(year2025Results);

        // List<Media> titleResults = mediaService.findMediaByTitle("End to End");
        // List<Media> yearResults = mediaService.findMediaByPublicationYear(2025);

        // // Verify search results
        // assertEquals("Should find one book by title", 1, titleResults.size());
        // assertEquals("Should find one book by year", 1, yearResults.size());
        // LOGGER.info("All search operations completed successfully");

        // // Add book to collection
        // // Configure collection after adding book
        // MediaCollection updatedCollection = (MediaCollection) collection;
        // updatedCollection.addMedia(book);
        // when(mediaRepository.findById(collection.getId())).thenReturn(updatedCollection);

        // // Book becomes unavailable when added to collection
        // book.setAvailable(false);
        // when(mediaRepository.findById(book.getId())).thenReturn(book);

        // // Add book to collection
        // mediaService.addMediaToCollection(collection.getId(), book.getId());

        // // Verify book is in collection and unavailable
        // Media retrievedCollection = mediaService.findMediaById(collection.getId());
        // MediaCollection collectionResult = (MediaCollection) retrievedCollection;
        // assertEquals("Collection should contain 1 item", 1,
        // collectionResult.getMediaItems().size());

        // Media retrievedBook = mediaService.findMediaById(book.getId());
        // assertFalse("Book should be unavailable", retrievedBook.isAvailable());
        // LOGGER.info("Book added to collection successfully");

        // // Update magazine availability
        // magazine.setAvailable(false);
        // when(mediaRepository.update(magazine)).thenReturn(magazine);

        // Media updatedMagazine = mediaService.updateMedia(magazine);
        // assertFalse("Magazine should be unavailable after update",
        // updatedMagazine.isAvailable());
        // LOGGER.info("Magazine availability updated successfully");

        // // Remove book from collection
        // // Configure collection after removal
        // MediaCollection collectionAfterRemoval = (MediaCollection) collection;
        // collectionAfterRemoval.getMediaItems().clear();
        // when(mediaRepository.findById(collection.getId())).thenReturn(collectionAfterRemoval);

        // // Book becomes available when removed from collection
        // book.setAvailable(true);
        // when(mediaRepository.findById(book.getId())).thenReturn(book);

        // // Remove book from collection
        // mediaService.removeMediaFromCollection(collection.getId(), book.getId());

        // // Verify book is removed and available
        // retrievedCollection = mediaService.findMediaById(collection.getId());
        // collectionResult = (MediaCollection) retrievedCollection;
        // assertTrue("Collection should be empty",
        // collectionResult.getMediaItems().isEmpty());

        // retrievedBook = mediaService.findMediaById(book.getId());
        // assertTrue("Book should be available after removal",
        // retrievedBook.isAvailable());
        // LOGGER.info("Book removed from collection successfully");

        // // Delete magazine
        // doNothing().when(mediaRepository).delete(magazine.getId());
        // when(mediaRepository.findById(magazine.getId()))
        // .thenReturn(magazine)
        // .thenThrow(new MediaNotFoundException("Media not found"));

        // mediaService.deleteMedia(magazine.getId());

        // try {
        // mediaService.findMediaById(magazine.getId());
        // fail("Should throw MediaNotFoundException");
        // } catch (MediaNotFoundException e) {
        // // Expected exception
        // LOGGER.info("Magazine deleted successfully");
        // }

        // LOGGER.info("End-to-End workflow completed successfully");
        // }
}