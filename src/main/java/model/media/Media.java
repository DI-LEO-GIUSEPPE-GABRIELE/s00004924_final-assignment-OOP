package model.media;

import java.io.Serializable;
import java.time.LocalDate;

// Interface for Media elements in lirary implementing Composite pattern
public interface Media extends Serializable {
    /**
     * Get the unique identifier of the media
     * 
     * @return the unique identifier of the media
     */
    String getId();

    /**
     * Get the title of the media
     * 
     * @return the title of the media
     */
    String getTitle();

    /**
     * Get the publication date of the media
     * 
     * @return the publication date of the media
     */
    LocalDate getPublicationDate();

    /**
     * Check if the media is available for loan
     * 
     * @return true or false if the media is available
     */
    boolean isAvailable();

    /**
     * Set the availability status of the media
     * 
     * @param available the availability status of the media to set
     */
    void setAvailable(boolean available);

    /**
     * Get detailed information about the media
     * 
     * @return a string with detailed information about the media
     */
    String getDetails();
}