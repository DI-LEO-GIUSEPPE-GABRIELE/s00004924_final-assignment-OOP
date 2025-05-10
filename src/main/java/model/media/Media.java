package model.media;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Interfaccia che rappresenta un elemento multimediale nella biblioteca.
 * Parte del pattern Composite.
 */
public interface Media extends Serializable {
    String getId();
    String getTitle();
    LocalDate getPublicationDate();
    boolean isAvailable();
    void setAvailable(boolean available);
    String getDetails();
}