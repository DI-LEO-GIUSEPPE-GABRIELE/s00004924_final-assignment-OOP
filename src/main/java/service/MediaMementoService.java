package service;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import memento.MediaMemento;
import model.media.Media;

// Memento service for saving and restoring Media object states
public class MediaMementoService {
    private static MediaMementoService instance;
    private final Map<String, Stack<MediaMemento>> history;

    private MediaMementoService() {
        this.history = new HashMap<>();
    }

    public static synchronized MediaMementoService getInstance() {
        if (instance == null) {
            instance = new MediaMementoService();
        }
        return instance;
    }

    /**
     * Save the state of a media
     * 
     * @param media : The media to save
     */
    public void saveState(Media media) {
        String mediaId = media.getId();
        MediaMemento memento = new MediaMemento(media);

        if (!history.containsKey(mediaId)) {
            history.put(mediaId, new Stack<>());
        }

        history.get(mediaId).push(memento);
    }

    /**
     * Verify if a media has a previous state
     * 
     * @param mediaId : The media id
     * @return true or false if the media has a previous state
     */
    public boolean hasPreviousState(String mediaId) {
        return history.containsKey(mediaId) && !history.get(mediaId).isEmpty();
    }

    /**
     * Get the last state of a media
     * 
     * @param mediaId : The media id
     * @return the last state of the media (memento)
     */
    public MediaMemento getLastState(String mediaId) {
        if (!hasPreviousState(mediaId)) {
            return null;
        }

        return history.get(mediaId).pop();
    }

    /**
     * Clear the history of a media
     * 
     * @param mediaId : The media id
     */
    public void clearHistory(String mediaId) {
        if (history.containsKey(mediaId)) {
            history.remove(mediaId);
        }
    }
}