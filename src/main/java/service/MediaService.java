package service;
import exception.LibraryException;
import exception.MediaNotFoundException;
import model.media.Book;
import model.media.Media;
import model.media.MediaCollection;
import repository.MediaRepository;
import util.LoggerManager;
import java.util.List;
import java.util.logging.Logger;

/**
 * Servizio per la gestione dei media.
 * Implementa la logica di business tra l'interfaccia utente e il repository.
 */
public class MediaService {
    private static final Logger LOGGER = LoggerManager.getLogger(MediaService.class.getName());
    private static MediaService instance;
    private final MediaRepository mediaRepository;
    
    private MediaService() {
        this.mediaRepository = MediaRepository.getInstance();
        LOGGER.info("Inizializzazione del servizio media");
    }
    
    /**
     * Ottiene l'istanza singleton del servizio.
     * 
     * @return l'istanza del servizio
     */
    public static synchronized MediaService getInstance() {
        if (instance == null) {
            instance = new MediaService();
        }
        return instance;
    }
    
    /**
     * Salva un media nel repository.
     * 
     * @param media il media da salvare
     * @return il media salvato
     * @throws LibraryException se si verifica un errore durante il salvataggio
     */
    public Media saveMedia(Media media) throws LibraryException {
        LOGGER.info("Richiesta di salvataggio media: " + media.getTitle());
        return mediaRepository.save(media);
    }
    
    /**
     * Trova un media per ID.
     * 
     * @param id l'ID del media da trovare
     * @return il media trovato
     * @throws MediaNotFoundException se il media non viene trovato
     */
    public Media findMediaById(String id) throws MediaNotFoundException {
        LOGGER.info("Ricerca media per ID: " + id);
        return mediaRepository.findById(id);
    }
    
    /**
     * Trova tutti i media nel repository.
     * 
     * @return una lista di tutti i media
     * @throws LibraryException se si verifica un errore durante la ricerca
     */
    public List<Media> findAllMedia() throws LibraryException {
        LOGGER.info("Richiesta di tutti i media");
        return mediaRepository.findAll();
    }
    
    /**
     * Elimina un media dal repository.
     * 
     * @param id l'ID del media da eliminare
     * @throws MediaNotFoundException se il media non viene trovato
     */
    public void deleteMedia(String id) throws MediaNotFoundException {
        LOGGER.info("Richiesta di eliminazione media con ID: " + id);
        mediaRepository.delete(id);
    }
    
    /**
     * Aggiorna un media nel repository.
     * 
     * @param media il media da aggiornare
     * @return il media aggiornato
     * @throws MediaNotFoundException se il media non viene trovato
     */
    public Media updateMedia(Media media) throws MediaNotFoundException {
        LOGGER.info("Richiesta di aggiornamento media: " + media.getTitle());
        return mediaRepository.update(media);
    }
    
    /**
     * Trova i media per titolo.
     * 
     * @param title il titolo da cercare
     * @return una lista di media con il titolo specificato
     */
    public List<Media> findMediaByTitle(String title) {
        LOGGER.info("Ricerca media per titolo: " + title);
        return mediaRepository.findByTitle(title);
    }
    
    /**
     * Trova i libri per autore.
     * 
     * @param author l'autore da cercare
     * @return una lista di libri dell'autore specificato
     */
    public List<Book> findBooksByAuthor(String author) {
        LOGGER.info("Ricerca libri per autore: " + author);
        return mediaRepository.findByAuthor(author);
    }
    
    /**
     * Trova i media per anno di pubblicazione.
     * 
     * @param year l'anno di pubblicazione da cercare
     * @return una lista di media pubblicati nell'anno specificato
     */
    public List<Media> findMediaByPublicationYear(int year) {
        LOGGER.info("Ricerca media per anno di pubblicazione: " + year);
        return mediaRepository.findByPublicationYear(year);
    }
    
    /**
     * Aggiunge un media a una collezione.
     * 
     * @param collectionId l'ID della collezione
     * @param mediaId l'ID del media da aggiungere
     * @throws MediaNotFoundException se la collezione o il media non vengono trovati
     * @throws LibraryException se si verifica un errore durante l'aggiunta
     */
    public void addMediaToCollection(String collectionId, String mediaId) throws MediaNotFoundException, LibraryException {
        Media collection = mediaRepository.findById(collectionId);
        Media media = mediaRepository.findById(mediaId);
        
        if (!(collection instanceof MediaCollection)) {
            throw new LibraryException("L'ID specificato non corrisponde a una collezione");
        }
        
        ((MediaCollection) collection).addMedia(media);
        mediaRepository.update(collection);
        LOGGER.info("Media " + mediaId + " aggiunto alla collezione " + collectionId);
    }
    
    /**
     * Rimuove un media da una collezione.
     * 
     * @param collectionId l'ID della collezione
     * @param mediaId l'ID del media da rimuovere
     * @throws MediaNotFoundException se la collezione o il media non vengono trovati
     * @throws LibraryException se si verifica un errore durante la rimozione
     */
    public void removeMediaFromCollection(String collectionId, String mediaId) throws MediaNotFoundException, LibraryException {
        Media collection = mediaRepository.findById(collectionId);
        Media media = mediaRepository.findById(mediaId);
        
        if (!(collection instanceof MediaCollection)) {
            throw new LibraryException("L'ID specificato non corrisponde a una collezione");
        }
        
        ((MediaCollection) collection).removeMedia(media);
        mediaRepository.update(collection);
        LOGGER.info("Media " + mediaId + " rimosso dalla collezione " + collectionId);
    }
}