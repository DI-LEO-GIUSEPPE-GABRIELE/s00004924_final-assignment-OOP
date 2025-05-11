package iterator;

import model.media.Media;
import model.media.MediaCollection;
import java.util.List;
import java.util.NoSuchElementException;

// Iterator for MediaCollections, implementing the Iterator pattern
public class MediaCollectionIterator implements MediaIterator {
    private final List<Media> mediaItems;
    private int position = 0;

    public MediaCollectionIterator(MediaCollection collection) {
        this.mediaItems = collection.getMediaItems();
    }

    @Override
    public boolean hasNext() {
        return position < mediaItems.size();
    }

    @Override
    public Media next() {
        if (!hasNext()) {
            throw new NoSuchElementException("There are no more media items.");
        }
        return mediaItems.get(position++);
    }
}