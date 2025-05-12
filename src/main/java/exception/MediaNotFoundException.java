package exception;

// Exception class for media not found
public class MediaNotFoundException extends LibraryException {

    public MediaNotFoundException(String mediaId) {
        super("Media with ID " + mediaId + " not found");
    }
}