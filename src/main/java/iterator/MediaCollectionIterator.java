package iterator;

import model.media.Media;
import model.media.MediaCollection;
import java.util.List;
import java.util.NoSuchElementException;

// Iterator class for MediaCollections, implementing the Iterator pattern
public class MediaCollectionIterator implements MediaIterator {
    private final List<Media> mediaItems;
    private int position = 0;

    public MediaCollectionIterator(MediaCollection collection) {
        this.mediaItems = collection.getMediaItems();
    }

    @Override
    // Annotation: Override method of the MediaIterator interface
    public boolean hasNext() {
        return position < mediaItems.size();
    }

    @Override
    // Annotation: Override method of the MediaIterator interface
    public Media next() {
        if (!hasNext()) {
            throw new NoSuchElementException("There are no more media items.");
        }
        return mediaItems.get(position++);
    }
}