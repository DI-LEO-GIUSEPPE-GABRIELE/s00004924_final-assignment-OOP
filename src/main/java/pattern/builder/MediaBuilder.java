package pattern.builder;

import model.media.Book;
import model.media.Magazine;
import model.media.MediaCollection;
import java.time.LocalDate;
import java.util.UUID;

// Builder pattern implementation for creating Media objects
public class MediaBuilder {
    private String id;
    private String title;
    private String author;
    private LocalDate publicationDate;
    private String publisher;
    private int pages;
    private int issue;

    public MediaBuilder() {
        this.id = UUID.randomUUID().toString();
    }

    public MediaBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public MediaBuilder withAuthor(String author) {
        this.author = author;
        return this;
    }

    public MediaBuilder withPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }

    public MediaBuilder withPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public MediaBuilder withPages(int pages) {
        this.pages = pages;
        return this;
    }

    public MediaBuilder withIssue(int issue) {
        this.issue = issue;
        return this;
    }

    /**
     * Build a Book object
     * 
     * @return a new Book instance
     */
    public Book buildBook() {
        if (title == null || author == null || publicationDate == null || publisher == null) {
            throw new IllegalStateException("Book is missing required properties");
        }
        return new Book(id, title, author, publicationDate, publisher, pages);
    }

    /**
     * Build a Magazine object
     * 
     * @return a new Magazine instance
     */
    public Magazine buildMagazine() {
        if (title == null || publicationDate == null || publisher == null) {
            throw new IllegalStateException("Magazine is missing required properties");
        }
        return new Magazine(id, title, publicationDate, publisher, issue);
    }

    /**
     * Build a MediaCollection object
     * 
     * @return a new MediaCollection instance
     */
    public MediaCollection buildCollection() {
        if (title == null || publicationDate == null) {
            throw new IllegalStateException("Collection is missing required properties");
        }

        return new MediaCollection(id, title, publicationDate);
    }
}