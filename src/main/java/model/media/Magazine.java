package model.media;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Classe che rappresenta una rivista.
 * Implementa l'interfaccia Media come parte del pattern Composite.
 */
public class Magazine implements Media, Serializable {
    private static final long serialVersionUID = 2L;
    private final String id;
    private final String title;
    private final String issn;
    private final LocalDate publicationDate;
    private final String publisher;
    private final int issue;
    private boolean available;

    public Magazine(String id, String title, String issn, LocalDate publicationDate, 
                   String publisher, int issue) {
        this.id = id;
        this.title = title;
        this.issn = issn;
        this.publicationDate = publicationDate;
        this.publisher = publisher;
        this.issue = issue;
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

    public String getIssn() {
        return issn;
    }

    @Override
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
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String getDetails() {
        return String.format("Rivista: %s, ISSN: %s, Pubblicato: %s, Editore: %s, Numero: %d, Disponibile: %s",
                title, issn, publicationDate, publisher, issue, available ? "Sì" : "No");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Magazine magazine = (Magazine) o;
        return Objects.equals(id, magazine.id);
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