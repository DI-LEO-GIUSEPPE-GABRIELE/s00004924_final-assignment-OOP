package model.media;

import java.time.LocalDate;
import java.util.Objects;

// Magazine class implementing the Media interface as part of the Composite pattern
public class Magazine implements Media {
    private static final long serialVersionUID = 2L;
    private final String id;
    private final String title;
    private final LocalDate publicationDate;
    private final String publisher;
    private final int issue;
    private boolean available;

    /**
     * Magazine constructor
     * 
     * @param id              - The unique identifier of the magazine
     * @param title           - The title of the magazine
     * @param publicationDate - The publication date of the magazine
     * @param publisher       - The publisher of the magazine
     * @param issue           - The issue number of the magazine
     */
    public Magazine(String id, String title, LocalDate publicationDate,
            String publisher, int issue) {
        this.id = id;
        this.title = title;
        this.publicationDate = publicationDate;
        this.publisher = publisher;
        this.issue = issue;
        this.available = true;
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Get the unique identifier of the magazine
     * 
     * @return the unique identifier of the magazine
     */
    public String getId() {
        return id;
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Get the title of the magazine
     * 
     * @return the title of the magazine
     */
    public String getTitle() {
        return title;
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Get the publication date of the magazine
     * 
     * @return the publication date of the magazine
     */
    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    /**
     * Get the publisher of the magazine
     * 
     * @return the publisher of the magazine
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Get the issue number of the magazine
     * 
     * @return the issue number of the magazine
     */
    public int getIssue() {
        return issue;
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Check if the magazine is available for loan
     * 
     * @return true or false if the magazine is available
     */
    public boolean isAvailable() {
        return available;
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Set the availability status of the magazine
     * 
     * @param available - The availability status of the magazine to set
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Get detailed information about the magazine
     * 
     * @return a string with detailed information about the magazine
     */
    public String getDetails() {
        return String.format("Magazine: %s, ID: %s, Publication date: %s, Publisher: %s, Number: %d, Available: %s",
                title, id, publicationDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                publisher, issue, available ? "Yes" : "No");
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Compare this magazine with another object for equality
     * 
     * @param o - The object to compare with
     * @return true if the objects are equal, false for the other cases
     */
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Magazine magazine = (Magazine) o;
        return Objects.equals(id, magazine.id);
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Generate a hash code for this magazine
     * 
     * @return the hash code for this magazine
     */
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Get a string representation of this magazine
     * 
     * @return a string representation of this magazine
     */
    public String toString() {
        return getDetails();
    }
}