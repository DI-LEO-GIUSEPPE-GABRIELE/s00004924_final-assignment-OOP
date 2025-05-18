package pattern.factory;

import model.media.Media;
import model.media.Book;
import model.media.Magazine;
import model.media.MediaCollection;
import java.time.LocalDate;
import java.util.UUID;

// Abstract Factory pattern implementation for creating different types of media factories
public class MediaFactoryProvider {

    /**
     * Get a factory for the specified media type
     * 
     * @param mediaType : The type of media factory to get
     * @return the appropriate media factory
     */
    public static MediaFactoryInterface getFactory(String mediaType) {
        switch (mediaType.toLowerCase()) {
            case "book":
                return new BookFactory();
            case "magazine":
                return new MagazineFactory();
            default:
                throw new IllegalArgumentException("Unknown media type: " + mediaType);
        }
    }

    // Interface for media factory
    public interface MediaFactoryInterface {
        Media createMedia();

        Media createBook(String title, String author, LocalDate publicationDate, String publisher, int pages);

        Media createMagazine(String title, LocalDate publicationDate, String publisher, int issue);

        MediaCollection createMediaCollection(String title);
    }

    // Book factory implementation
    private static class BookFactory implements MediaFactoryInterface {
        public Media createMedia() {
            return new Book(generateId(), "", "", LocalDate.now(), "", 0);
        }

        public Media createBook(String title, String author, LocalDate publicationDate, String publisher, int pages) {
            String id = generateId();
            return new Book(id, title, author, publicationDate, publisher, pages);
        }

        public Media createMagazine(String title, LocalDate publicationDate, String publisher, int issue) {
            throw new UnsupportedOperationException("BookFactory cannot create magazines");
        }

        public MediaCollection createMediaCollection(String title) {
            String id = generateId();
            return new MediaCollection(id, title, LocalDate.now());
        }
    }

    // Magazine factory implementation
    private static class MagazineFactory implements MediaFactoryInterface {
        public Media createMedia() {
            return new Magazine(generateId(), "", LocalDate.now(), "", 0);
        }

        public Media createBook(String title, String author, LocalDate publicationDate, String publisher, int pages) {
            throw new UnsupportedOperationException("MagazineFactory cannot create books");
        }

        public Media createMagazine(String title, LocalDate publicationDate, String publisher, int issue) {
            String id = generateId();
            return new Magazine(id, title, publicationDate, publisher, issue);
        }

        public MediaCollection createMediaCollection(String title) {
            String id = generateId();
            return new MediaCollection(id, title, LocalDate.now());
        }
    }

    /**
     * Generate a unique ID
     * 
     * @return a unique ID as a string
     */
    private static String generateId() {
        return UUID.randomUUID().toString();
    }
}