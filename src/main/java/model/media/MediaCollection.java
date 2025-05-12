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

    // MediaCollection constructor
    public MediaCollection(String id, String title, LocalDate creationDate) {
        this.id = id;
        this.title = title;
        this.creationDate = creationDate;
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
        return creationDate;
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
        if (!available) {
            for (Media media : mediaItems) {
                media.setAvailable(false);
            }
        }
    }

    @Override
    // Override method of the Media interface
    public String getDetails() {
        return String.format("Collection: %s, Created: %s, Elements: %d, Available: %s",
                title, creationDate, mediaItems.size(), available ? "Yes" : "No");
    }

    public void addMedia(Media media) {
        mediaItems.add(media);
    }

    public void removeMedia(Media media) {
        mediaItems.remove(media);
    }

    public List<Media> getMediaItems() {
        return new ArrayList<>(mediaItems);
    }

    public MediaIterator createIterator() {
        return new MediaCollectionIterator(this);
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