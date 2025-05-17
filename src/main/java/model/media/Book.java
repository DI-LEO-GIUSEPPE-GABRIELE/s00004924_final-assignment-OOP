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

    /**
     * Book constructor
     * 
     * @param id              - The unique identifier of the book
     * @param title           - The title of the book
     * @param author          - The author of the book
     * @param publicationDate - The publication date of the book
     * @param publisher       - The publisher of the book
     * @param pages           - The number of pages in the book
     */
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
    /**
     * Annotation: Override method of the Media interface
     * Get the unique identifier of the book
     * 
     * @return the unique identifier of the book
     */
    public String getId() {
        return id;
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Get the title of the book
     * 
     * @return the the title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the author of the book
     * 
     * @return the author of the book
     */
    public String getAuthor() {
        return author;
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Get the publication date of the book
     * 
     * @return the publication date of the book
     */
    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    /**
     * Get the publisher of the book
     * 
     * @return the publisher of the book
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Get the number of pages in the book
     * 
     * @return the number of pages in the book
     */
    public int getPages() {
        return pages;
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Check if the book is available for loan
     * 
     * @return true or false if the book is available
     */
    public boolean isAvailable() {
        return available;
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Set the availability status of the book
     * 
     * @param available - The availability status of the book to set
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Get detailed information about the book
     * 
     * @return a string with detailed information about the book
     */
    public String getDetails() {
        return String.format(
                "Book: %s, Author: %s, ID: %s, Publication date: %s, Publisher: %s, Pages: %d, Available: %s",
                title, author, id, publicationDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                publisher, pages, available ? "Yes" : "No");
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Compare this book with another object for equality
     * 
     * @param o - The object to compare with
     * @return true if the objects are equal, false for the other cases
     */
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Generate a hash code for this book
     * 
     * @return the hash code for this book
     */
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Get a string representation of this book
     * 
     * @return a string representation of this book
     */
    public String toString() {
        return getDetails();
    }
}