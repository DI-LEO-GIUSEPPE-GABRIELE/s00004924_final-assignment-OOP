package factory;

import model.media.MediaCollection;
import model.media.Book;
import model.media.Magazine;
import model.media.Media;
import java.time.LocalDate;
import java.util.UUID;

// Factory class for creation of books and magazines
public class MediaFactory {

    /**
     * Create a book
     * 
     * @param title           - The title of the book
     * @param author          - The author of the book
     * @param publicationDate - The publication date of the book
     * @param publisher       - The publisher of the book
     * @param pages           - The number of pages in the book
     * @return a new Book
     */
    public static Media createBook(String title, String author,
            LocalDate publicationDate, String publisher, int pages) {
        String id = generateId();
        return new Book(id, title, author, publicationDate, publisher, pages);
    }

    /**
     * Create a magazine
     * 
     * @param title           - The title of the magazine
     * @param publicationDate - The publication date of the magazine
     * @param publisher       - The publisher of the magazine
     * @param issue           - The issue number of the magazine
     * @return a new Magazine
     */
    public static Media createMagazine(String title, LocalDate publicationDate,
            String publisher, int issue) {
        String id = generateId();
        return new Magazine(id, title, publicationDate, publisher, issue);
    }

    /**
     * Create a media collection
     * 
     * @param title - The title of the media collection
     * @return a new MediaCollection
     */
    public static MediaCollection createMediaCollection(String title) {
        String id = generateId();
        return new MediaCollection(id, title, LocalDate.now());
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