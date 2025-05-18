package pattern.memento;

import model.media.Media;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

// Memento pattern implementation for saving and restoring Media object states
public class MediaMemento {
    private final String id;
    private final Map<String, Object> state = new HashMap<>();

    /**
     * Create a new memento to store the state of a media object
     * 
     * @param media : The media object to save the state of
     */
    public MediaMemento(Media media) {
        this.id = media.getId();
        state.put("title", media.getTitle());
        state.put("publicationDate", media.getPublicationDate());

        try {
            if (media.getClass().getSimpleName().equals("Book")) {
                state.put("author", media.getClass().getMethod("getAuthor").invoke(media));
                state.put("pages", media.getClass().getMethod("getPages").invoke(media));
            } else if (media.getClass().getSimpleName().equals("Magazine")) {
                state.put("issue", media.getClass().getMethod("getIssue").invoke(media));
            }
        } catch (Exception e) {
            System.err.println("Error capturing media state: " + e.getMessage());
        }
    }

    /**
     * Get the ID of the media
     * 
     * @return the media ID
     */
    public String getId() {
        return id;
    }

    /**
     * Get a property from the saved state
     * 
     * @param key : The property name
     * @return the property value
     */
    public Object getState(String key) {
        return state.get(key);
    }

    /**
     * Get the title
     * 
     * @return the title
     */
    public String getTitle() {
        return (String) state.get("title");
    }

    /**
     * Get the publication date
     * 
     * @return the publication date
     */
    public LocalDate getPublicationDate() {
        return (LocalDate) state.get("publicationDate");
    }

    /**
     * Get the publisher
     * 
     * @return the publisher
     */
    public String getPublisher() {
        return (String) state.get("publisher");
    }

    /**
     * Get the pages (for books)
     * 
     * @return the pages or null if not a book
     */
    public Integer getPages() {
        return (Integer) state.get("pages");
    }
}