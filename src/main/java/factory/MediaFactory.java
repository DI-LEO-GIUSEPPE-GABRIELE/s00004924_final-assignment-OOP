package factory;
import model.media.Book;
import model.media.Magazine;
import model.media.Media;
import java.time.LocalDate;
import java.util.UUID;

// Factory for creation of books and magazines
public class MediaFactory {

    // Method for creating a book
    public static Media createBook(String title, String author,
            LocalDate publicationDate, String publisher, int pages) {
        String id = generateId();
        return new Book(id, title, author, publicationDate, publisher, pages);
    }

    // Method for creating a magazine
    public static Media createMagazine(String title, LocalDate publicationDate,
            String publisher, int issue) {
        String id = generateId();
        return new Magazine(id, title, publicationDate, publisher, issue);
    }

    // Private method for generating a unique ID
    private static String generateId() {
        return UUID.randomUUID().toString();
    }
}