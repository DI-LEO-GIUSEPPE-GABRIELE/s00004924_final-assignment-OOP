package exception;

// Exception for media not found
public class MediaNotFoundException extends LibraryException {
    
    public MediaNotFoundException(String mediaId) {
        super("Media with ID " + mediaId + " not found");
    }
}