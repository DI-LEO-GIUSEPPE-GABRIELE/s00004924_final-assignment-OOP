package model.media;

import java.time.LocalDate;
import java.util.Objects;

// Book class implementing the Media interface as part of the Composite pattern
public class Book implements Media {
    private static final long serialVersionUID = 1L;
    private final String id;
    private final String title;
    private final String author;
    private final LocalDate publicationDate;
    private final String publisher;
    private final int pages;
    private boolean available;

    // Book constructor
    public Book(String id, String title, String author,
            LocalDate publicationDate, String publisher, int pages) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.publisher = publisher;
        this.pages = pages;
        this.available = true;
    }

    @Override
    // Override method of the Media interface
    public String getId() {
        return id;
    }

    @Override
    // Override method of the Media interface
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    // Override method of the Media interface
    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getPages() {
        return pages;
    }

    @Override
    // Override method of the Media interface
    public boolean isAvailable() {
        return available;
    }

    @Override
    // Override method of the Media interface
    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    // Override method of the Media interface
    public String getDetails() {
        return String.format("Book: %s by %s, Publication date: %s, Publisher: %s, Pages: %d, Available: %s",
                title, author, publicationDate, publisher, pages, available ? "Yes" : "No");
    }

    @Override
    // Override method of the Media interface
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    // Override method of the Media interface
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    // Override method of the Media interface
    public String toString() {
        return getDetails();
    }
}