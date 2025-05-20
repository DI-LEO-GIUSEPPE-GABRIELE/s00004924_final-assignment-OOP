package strategy;

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

        // Using Stream API with lambda and Comparator to sort the list by publication
        // date in
        // descending order
        List<Media> sortedList = mediaList.stream()
                .sorted(Comparator.comparing(Media::getPublicationDate).reversed())
                .collect(java.util.stream.Collectors.toList());

        LOGGER.info("Media sorted by publication date successfully (desc)");

        return sortedList;
    }

    @Override
    public String getSortName() {
        return "Sort by Publication Date";
    }
}