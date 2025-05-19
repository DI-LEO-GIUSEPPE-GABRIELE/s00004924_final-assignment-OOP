package factory;

import model.media.MediaCollection;
import model.media.Book;
import model.media.Magazine;
import pattern.builder.MediaBuilder;
import java.time.LocalDate;

// Factory class for creation of books and magazines
public class MediaFactory {

    /**
     * Create a book
     * 
     * @param title           : The title of the book
     * @param author          : The author of the book
     * @param publicationDate : The publication date of the book
     * @param publisher       : The publisher of the book
     * @param pages           : The number of pages in the book
     * @return a new Book
     */
    public static Book createBook(String title, String author,
            LocalDate publicationDate, String publisher, int pages) {
        return new MediaBuilder()
                .withTitle(title)
                .withAuthor(author)
                .withPublicationDate(publicationDate)
                .withPublisher(publisher)
                .withPages(pages)
                .buildBook();
    }

    /**
     * Create a magazine
     * 
     * @param title           : The title of the magazine
     * @param publicationDate : The publication date of the magazine
     * @param publisher       : The publisher of the magazine
     * @param issue           : The issue number of the magazine
     * @return a new Magazine
     */
    public static Magazine createMagazine(String title, LocalDate publicationDate,
            String publisher, int issue) {
        return new MediaBuilder()
                .withTitle(title)
                .withPublicationDate(publicationDate)
                .withPublisher(publisher)
                .withIssue(issue)
                .buildMagazine();
    }

    /**
     * Create a media collection
     * 
     * @param title : The title of the media collection
     * @return a new MediaCollection
     */
    public static MediaCollection createMediaCollection(String title) {
        return new MediaBuilder()
                .withTitle(title)
                .withPublicationDate(LocalDate.now())
                .buildCollection();
    }
}