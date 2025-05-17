package model.media;

import iterator.MediaIterator;
import iterator.MediaCollectionIterator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Class for MediaCollection implementing the Media interface as Composite pattern
public class MediaCollection implements Media {
    private static final long serialVersionUID = 3L;
    private final String id;
    private final String title;
    private final LocalDate creationDate;
    private boolean available;
    private final List<Media> mediaItems = new ArrayList<>();

    /**
     * MediaCollection constructor
     * 
     * @param id           - The unique identifier of the collection
     * @param title        - The title of the collection
     * @param creationDate - The creation date of the collection
     */
    public MediaCollection(String id, String title, LocalDate creationDate) {
        this.id = id;
        this.title = title;
        this.creationDate = creationDate;
        this.available = true;
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Get the unique identifier of the collection
     * 
     * @return the unique identifier of the collection
     */
    public String getId() {
        return id;
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Get the title of the collection
     * 
     * @return the title of the collection
     */
    public String getTitle() {
        return title;
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Get the creation date of the collection
     * 
     * @return the creation date of the collection
     */
    public LocalDate getPublicationDate() {
        return creationDate;
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Check if the collection is available for loan
     * 
     * @return true or false if the collection is available
     */
    public boolean isAvailable() {
        return available;
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Set the availability status of the collection and all its items
     * 
     * @param the availability status of the collection to set
     */
    public void setAvailable(boolean available) {
        this.available = available;
        if (!available) {
            for (Media media : mediaItems) {
                media.setAvailable(false);
            }
        }
    }

    @Override
    /**
     * Annotation: Override method of the Media interface
     * Get detailed information about the collection
     * 
     * @return a string with detailed information about the collection
     */
    public String getDetails() {
        return String.format("Collection: %s, ID: %s, Created: %s, Elements: %d, Available: %s",
                title, id, creationDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                mediaItems.size(), available ? "Yes" : "No");
    }

    /**
     * Add a media item to the collection
     * 
     * @param media - The media item to add
     */
    public void addMedia(Media media) {
        // Verify if the media is not already in the collection
        if (!mediaItems.contains(media)) {
            mediaItems.add(media);
        }
    }

    /**
     * Remove a media item from the collection
     * 
     * @param media - The media item to remove
     */
    public void removeMedia(Media media) {
        mediaItems.remove(media);
    }

    /**
     * Get all media items in the collection
     * 
     * @return a list of all media items in the collection
     */
    public List<Media> getMediaItems() {
        return new ArrayList<>(mediaItems);
    }

    /**
     * Create an iterator for the collection
     * 
     * @return a new MediaIterator instance, an iterator for the collection
     */
    public MediaIterator createIterator() {
        return new MediaCollectionIterator(this);
    }

    /**
     * Check if the collection contains a media item with the given ID
     * 
     * @param mediaId - The ID of the media item to check
     * @return true or false if the collection contains the media item
     */
    public boolean containsMedia(String mediaId) {
        for (Media media : mediaItems) {
            if (media.getId().equals(mediaId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    // Override method of the Media interface
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MediaCollection that = (MediaCollection) o;
        return Objects.equals(id, that.id);
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