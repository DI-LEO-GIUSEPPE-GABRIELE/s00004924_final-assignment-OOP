package observer;

import model.media.Media;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Subject class for the Observer pattern, maintains a list of observers and
// notifies them of changes to media objects and demonstrates multithreading by
// using an ExecutorService to notify observers asynchronously
public class MediaChangeSubject {
    private final List<MediaChangeObserver> observers = new ArrayList<>();
    private final ExecutorService notificationExecutor = Executors.newFixedThreadPool(2);

    /**
     * Add an observer
     * 
     * @param observer : The observer to add
     */
    public void addObserver(MediaChangeObserver observer) {
        observers.add(observer);
    }

    /**
     * Remove an observer
     * 
     * @param observer : The observer to remove
     */
    public void removeObserver(MediaChangeObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notify all observers that a media item was added
     * Uses multithreading to notify observers asynchronously
     * 
     * @param media : The media that was added
     */
    public void notifyMediaAdded(final Media media) {
        for (final MediaChangeObserver observer : observers) {
            notificationExecutor.submit(() -> observer.onMediaAdded(media));
        }
    }

    /**
     * Notify all observers that a media item was removed
     * Uses multithreading to notify observers asynchronously
     * 
     * @param media : The media that was removed
     */
    public void notifyMediaRemoved(final Media media) {
        for (final MediaChangeObserver observer : observers) {
            notificationExecutor.submit(() -> observer.onMediaRemoved(media));
        }
    }

    /**
     * Notify all observers that a media item was updated
     * Uses multithreading to notify observers asynchronously
     * 
     * @param oldMedia : The old state of the media
     * @param newMedia : The new state of the media
     */
    public void notifyMediaUpdated(final Media oldMedia, final Media newMedia) {
        for (final MediaChangeObserver observer : observers) {
            notificationExecutor.submit(() -> observer.onMediaUpdated(oldMedia, newMedia));
        }
    }

    // Shutdown the executor service, called when the application is shutting down
    public void shutdown() {
        notificationExecutor.shutdown();
    }
}