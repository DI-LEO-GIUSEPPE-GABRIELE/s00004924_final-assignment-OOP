package observer;

import model.media.Media;

// Observer interface for the Observer pattern
public interface MediaChangeObserver {
    /**
     * Called when a media item is added
     * 
     * @param media : The media that was added
     */
    void onMediaAdded(Media media);

    /**
     * Called when a media item is removed
     * 
     * @param media : The media that was removed
     */
    void onMediaRemoved(Media media);

    /**
     * Called when a media item is updated
     * 
     * @param oldMedia : The old state of the media
     * @param newMedia : The new state of the media
     */
    void onMediaUpdated(Media oldMedia, Media newMedia);
}