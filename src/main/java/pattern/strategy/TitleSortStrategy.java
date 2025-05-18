package pattern.strategy;

import model.media.Media;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Strategy class for sorting media by title, using the Strategy pattern and Stream API with lambdas
public class TitleSortStrategy implements SortingStrategy {

    @Override
    public List<Media> sort(List<Media> mediaList) {
        // Using Stream API and lambda for sorting
        return mediaList.stream()
                .sorted(Comparator.comparing(Media::getTitle))
                .collect(Collectors.toList());
    }

    @Override
    public String getStrategyName() {
        return "Sort by Title";
    }
}