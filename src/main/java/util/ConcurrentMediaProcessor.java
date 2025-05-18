package util;

import model.media.Media;
import pattern.template.MediaProcessor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

// Utility class for concurrent processing of media items
public class ConcurrentMediaProcessor {
    private static final Logger LOGGER = LoggerManager.getLogger(ConcurrentMediaProcessor.class.getName());
    private final ExecutorService executorService;

    /**
     * Create a new ConcurrentMediaProcessor with a fixed thread pool
     * 
     * @param threadCount : The number of threads in the pool
     */
    public ConcurrentMediaProcessor(int threadCount) {
        this.executorService = Executors.newFixedThreadPool(threadCount);
        LOGGER.info("Created concurrent processor with " + threadCount + " threads");
    }

    /**
     * Process a list of media items concurrently using the provided processor
     * 
     * @param mediaList : The list of media items to process
     * @param processor : The processor to use for each media item
     * @return a list of results indicating success or failure for each media item
     */
    public List<Boolean> processConcurrently(List<Media> mediaList, MediaProcessor processor) {
        LOGGER.info("Starting concurrent processing of " + mediaList.size() + " media items");

        // Use Stream API with CompletableFuture for concurrent processing
        List<CompletableFuture<Boolean>> futures = mediaList.stream()
                .map(media -> CompletableFuture.supplyAsync(
                        () -> processor.processMedia(media),
                        executorService))
                .collect(Collectors.toList());

        // Wait for all futures to complete and collect results
        List<Boolean> results = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        LOGGER.info("Completed concurrent processing of " + mediaList.size() + " media items");
        return results;
    }

    /**
     * Process a list of media items concurrently using the provided function
     * 
     * @param <R>       : The type of the result
     * @param mediaList : The list of media items to process
     * @param function  : The function to apply to each media item
     * @return a list of results
     */
    public <R> List<R> processConcurrently(List<Media> mediaList, Function<Media, R> function) {
        LOGGER.info("Starting concurrent processing with custom function");

        // Use Stream API with CompletableFuture and a custom function
        List<CompletableFuture<R>> futures = mediaList.stream()
                .map(media -> CompletableFuture.supplyAsync(
                        () -> function.apply(media),
                        executorService))
                .collect(Collectors.toList());

        // Wait for all futures to complete and collect results
        List<R> results = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        LOGGER.info("Completed concurrent processing with custom function");
        return results;
    }

    /**
     * Shutdown the executor service
     * 
     * @param waitTimeSeconds : The maximum time to wait for termination in seconds
     * @return true if the executor service terminated, false if the timeout elapsed
     *         before termination
     */
    public boolean shutdown(int waitTimeSeconds) {
        LOGGER.info("Shutting down concurrent processor");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(waitTimeSeconds, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                LOGGER.warning("Forced shutdown of concurrent processor");
                return false;
            }
            LOGGER.info("Concurrent processor shutdown successfully");
            return true;
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
            LOGGER.severe("Concurrent processor shutdown interrupted: " + e.getMessage());
            return false;
        }
    }
}