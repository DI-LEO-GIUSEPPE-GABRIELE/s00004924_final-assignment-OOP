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

    // Magazine constructor
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
    // Override method of the Media interface
    public String getId() {
        return id;
    }

    @Override
    // Override method of the Media interface
    public String getTitle() {
        return title;
    }

    @Override
    // Override method of the Media interface
    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getIssue() {
        return issue;
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
        return String.format("Magazine: %s, Publication date: %s, Publisher: %s, Number: %d, Available: %s",
                title, publicationDate, publisher, issue, available ? "Yes" : "No");
    }

    @Override
    // Override method of the Media interface
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Magazine magazine = (Magazine) o;
        return Objects.equals(id, magazine.id);
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