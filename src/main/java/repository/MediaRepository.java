package repository;
import exception.LibraryException;
import exception.MediaNotFoundException;
import model.media.Book;
import model.media.Media;
import util.LoggerManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Implementazione del repository per i media.
 * Utilizza una mappa in memoria per memorizzare i media.
 */
public class MediaRepository implements Repository<Media, String> {
    private static final Logger LOGGER = LoggerManager.getLogger(MediaRepository.class.getName());
    private static MediaRepository instance;
    private final Map<String, Media> mediaMap;
    
    private MediaRepository() {
        this.mediaMap = new HashMap<>();
        LOGGER.info("Inizializzazione del repository dei media");
        loadMediaFromStorage();
    }
    
    /**
     * Carica i media dal file system.
     */
    private void loadMediaFromStorage() {
        try {
            FileStorageManager storageManager = FileStorageManager.getInstance();
            List<Media> mediaList = storageManager.loadMedia();
            
            for (Media media : mediaList) {
                mediaMap.put(media.getId(), media);
            }
            
            LOGGER.info("Media caricati dal file system: " + mediaList.size());
        } catch (LibraryException e) {
            LOGGER.warning("Impossibile caricare i media dal file system: " + e.getMessage());
        }
    }
    
    /**
     * Salva i media sul file system.
     */
    private void saveMediaToStorage() {
        try {
            FileStorageManager storageManager = FileStorageManager.getInstance();
            storageManager.saveMedia(mediaMap);
            LOGGER.info("Media salvati sul file system");
        } catch (LibraryException e) {
            LOGGER.warning("Impossibile salvare i media sul file system: " + e.getMessage());
        }
    }
    
    /**
     * Ottiene l'istanza singleton del repository.
     * 
     * @return l'istanza del repository
     */
    public static synchronized MediaRepository getInstance() {
        if (instance == null) {
            instance = new MediaRepository();
        }
        return instance;
    }
    
    @Override
    public Media save(Media media) throws LibraryException {
        if (media == null) {
            throw new LibraryException("Impossibile salvare un media nullo");
        }
        
        mediaMap.put(media.getId(), media);
        saveMediaToStorage();
        LOGGER.info("Media salvato con ID: " + media.getId());
        return media;
    }
    
    @Override
    public Media findById(String id) throws MediaNotFoundException {
        if (!mediaMap.containsKey(id)) {
            LOGGER.warning("Media non trovato con ID: " + id);
            throw new MediaNotFoundException("Media non trovato con ID: " + id);
        }
        
        return mediaMap.get(id);
    }
    
    @Override
    public List<Media> findAll() throws LibraryException {
        return new ArrayList<>(mediaMap.values());
    }
    
    @Override
    public void delete(String id) throws MediaNotFoundException {
        if (!mediaMap.containsKey(id)) {
            LOGGER.warning("Impossibile eliminare: media non trovato con ID: " + id);
            throw new MediaNotFoundException("Media non trovato con ID: " + id);
        }
        
        mediaMap.remove(id);
        saveMediaToStorage();
        LOGGER.info("Media eliminato con ID: " + id);
    }
    
    @Override
    public Media update(Media media) throws MediaNotFoundException {
        if (media == null || !mediaMap.containsKey(media.getId())) {
            LOGGER.warning("Impossibile aggiornare: media non trovato");
            throw new MediaNotFoundException("Media non trovato");
        }
        
        mediaMap.put(media.getId(), media);
        saveMediaToStorage();
        LOGGER.info("Media aggiornato con ID: " + media.getId());
        return media;
    }
    
    /**
     * Trova i media per titolo.
     * 
     * @param title il titolo da cercare
     * @return una lista di media con il titolo specificato
     */
    public List<Media> findByTitle(String title) {
        return mediaMap.values().stream()
                .filter(media -> media.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Trova i libri per autore.
     * 
     * @param author l'autore da cercare
     * @return una lista di libri dell'autore specificato
     */
    public List<Book> findByAuthor(String author) {
        return mediaMap.values().stream()
                .filter(media -> media instanceof Book)
                .map(media -> (Book) media)
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Trova i media per anno di pubblicazione.
     * 
     * @param year l'anno di pubblicazione da cercare
     * @return una lista di media pubblicati nell'anno specificato
     */
    public List<Media> findByPublicationYear(int year) {
        return mediaMap.values().stream()
                .filter(media -> media.getPublicationDate().getYear() == year)
                .collect(Collectors.toList());
    }
}