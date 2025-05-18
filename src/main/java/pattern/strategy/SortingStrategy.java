package pattern.strategy;

import model.media.Media;
import java.util.List;

// Strategy interface for different sorting algorithms, as part of the Strategy pattern implementation
public interface SortingStrategy {
    /**
     * Sort a list of media items
     * 
     * @param mediaList : The list to sort
     * @return the sorted list
     */
    List<Media> sort(List<Media> mediaList);

    /**
     * Get the strategy name
     * 
     * @return the strategy name
     */
    String getStrategyName();
}