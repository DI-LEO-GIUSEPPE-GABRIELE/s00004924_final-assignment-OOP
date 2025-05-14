package iterator;

import model.media.Media;

// Interface for media iterator, implementing the Iterator pattern
public interface MediaIterator {
    boolean hasNext();

    Media next();
}