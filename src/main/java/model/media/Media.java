package model.media;

import java.io.Serializable;
import java.time.LocalDate;

// Interface for Media elements in lirary implementing Composite pattern
public interface Media extends Serializable {
    String getId();

    String getTitle();

    LocalDate getPublicationDate();

    boolean isAvailable();

    void setAvailable(boolean available);

    String getDetails();
}