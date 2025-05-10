package factory;

import model.media.Book;
import model.media.Magazine;
import model.media.Media;
import model.media.MediaCollection;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Factory per la creazione di oggetti Media.
 * Implementa il pattern Factory.
 */
public class MediaFactory {

    public static Media createBook(String title, String author,
            LocalDate publicationDate, String publisher, int pages) {
        String id = generateId();
        return new Book(id, title, author, publicationDate, publisher, pages);
    }

    public static Media createMagazine(String title, LocalDate publicationDate,
            String publisher, int issue) {
        String id = generateId();
        return new Magazine(id, title, publicationDate, publisher, issue);
    }

    public static MediaCollection createMediaCollection(String title) {
        String id = generateId();
        return new MediaCollection(id, title, LocalDate.now());
    }

    private static String generateId() {
        return UUID.randomUUID().toString();
    }
}