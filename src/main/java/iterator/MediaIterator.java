package iterator;

import model.media.Media;

/**
 * Interfaccia per l'iteratore di media.
 * Implementa il pattern Iterator.
 */
public interface MediaIterator {
    boolean hasNext();
    Media next();
}