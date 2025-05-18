package pattern.adapter;

import model.media.Media;

// Adapter pattern implementation for adapting different media formats
public class MediaAdapter {
    private final Media adaptee;

    /**
     * Create a new MediaAdapter
     * 
     * @param adaptee : The media to adapt
     */
    public MediaAdapter(Media adaptee) {
        this.adaptee = adaptee;
    }

    /**
     * Get a formatted string representation of the media
     * 
     * @return a formatted string
     */
    public String getFormattedDetails() {
        StringBuilder builder = new StringBuilder();
        builder.append("ID: ").append(adaptee.getId()).append("\n");
        builder.append("Title: ").append(adaptee.getTitle()).append("\n");
        builder.append("Publication Date: ").append(adaptee.getPublicationDate()).append("\n");

        if (adaptee.getClass().getSimpleName().equals("Book")) {
            try {
                String author = (String) adaptee.getClass().getMethod("getAuthor").invoke(adaptee);
                int pages = (int) adaptee.getClass().getMethod("getPages").invoke(adaptee);
                builder.append("Author: ").append(author).append("\n");
                builder.append("Pages: ").append(pages).append("\n");
            } catch (Exception e) {
                builder.append("Error retrieving book details");
            }
        } else if (adaptee.getClass().getSimpleName().equals("Magazine")) {
            try {
                int issue = (int) adaptee.getClass().getMethod("getIssue").invoke(adaptee);
                builder.append("Issue: ").append(issue).append("\n");
            } catch (Exception e) {
                builder.append("Error retrieving magazine details");
            }
        }

        return builder.toString();
    }

    /**
     * Export the media to a different format
     * 
     * @param format : The format to export to
     * @return the exported string
     */
    public String exportTo(String format) {
        switch (format.toLowerCase()) {
            case "json":
                return toJson();
            case "xml":
                return toXml();
            default:
                return "Unsupported format: " + format;
        }
    }

    private String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"id\":\"" + adaptee.getId() + "\",");
        json.append("\"title\":\"" + adaptee.getTitle() + "\",");
        json.append("\"publicationDate\":\"" + adaptee.getPublicationDate() + "\",");
        json.append("}");
        return json.toString();
    }

    private String toXml() {
        StringBuilder xml = new StringBuilder();
        xml.append("<media>");
        xml.append("<id>" + adaptee.getId() + "</id>");
        xml.append("<title>" + adaptee.getTitle() + "</title>");
        xml.append("<publicationDate>" + adaptee.getPublicationDate() + "</publicationDate>");
        xml.append("</media>");
        return xml.toString();
    }
}