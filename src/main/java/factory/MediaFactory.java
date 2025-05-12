package factory;

import model.media.MediaCollection;
import model.media.Book;
import model.media.Magazine;
import model.media.Media;
import java.time.LocalDate;
import java.util.UUID;

// Factory class for creation of books and magazines
public class MediaFactory {

    // Create a book
    public static Media createBook(String title, String author,
            LocalDate publicationDate, String publisher, int pages) {
        String id = generateId();
        return new Book(id, title, author, publicationDate, publisher, pages);
    }

    // Create a magazine
    public static Media createMagazine(String title, LocalDate publicationDate,
            String publisher, int issue) {
        String id = generateId();
        return new Magazine(id, title, publicationDate, publisher, issue);
    }

    // Create a media collection
    public static MediaCollection createMediaCollection(String title) {
        String id = generateId();
        return new MediaCollection(id, title, LocalDate.now());
    }

    // Generate a unique ID
    private static String generateId() {
        return UUID.randomUUID().toString();
    }
}