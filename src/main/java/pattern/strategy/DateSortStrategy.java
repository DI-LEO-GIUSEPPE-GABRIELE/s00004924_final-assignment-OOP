package pattern.strategy;

import model.media.Media;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import util.LoggerManager;

// Strategy class for sorting media by publication date, using the Strategy pattern and Stream API with lambdas
public class DateSortStrategy implements SortingStrategy {
    private static final Logger LOGGER = LoggerManager.getLogger(DateSortStrategy.class.getName());

    @Override
    public List<Media> sort(List<Media> mediaList) {
        LOGGER.info("Sorting media by publication date (desc)");

        // Sort the list in-place using a Comparator in reverse order
        mediaList.sort(Comparator.comparing(Media::getPublicationDate).reversed());

        LOGGER.info("Media sorted by publication date successfully (desc)");

        return mediaList;
    }

    @Override
    public String getSortName() {
        return "Sort by Publication Date";
    }
}