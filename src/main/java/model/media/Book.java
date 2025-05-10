package model.media;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Classe che rappresenta un libro.
 * Implementa l'interfaccia Media come parte del pattern Composite.
 */
public class Book implements Media, Serializable {
    private static final long serialVersionUID = 1L;
    private final String id;
    private final String title;
    private final String author;
    private final LocalDate publicationDate;
    private final String publisher;
    private final int pages;
    private boolean available;

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
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    @Override
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
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String getDetails() {
        return String.format("Libro: %s di %s, Pubblicato: %s, Editore: %s, Pagine: %d, Disponibile: %s",
                title, author, publicationDate, publisher, pages, available ? "Sì" : "No");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getDetails();
    }
}