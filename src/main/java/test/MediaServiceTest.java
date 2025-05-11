package test;
import exception.LibraryException;
import exception.MediaNotFoundException;
import factory.MediaFactory;
import model.media.Book;
import model.media.Media;
import service.MediaService;
import util.LoggerManager;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

/**
 * Classe di test per il servizio MediaService.
 * Verifica il corretto funzionamento delle operazioni CRUD e delle ricerche.
 */
public class MediaServiceTest {
    private static final Logger LOGGER = LoggerManager.getLogger(MediaServiceTest.class.getName());
    private final MediaService mediaService;
    
    public MediaServiceTest() {
        this.mediaService = MediaService.getInstance();
    }
    
    /**
     * Esegue i test del servizio MediaService.
     */
    public void runTests() {
        LOGGER.info("Inizio dei test del servizio MediaService");
        
        try {
            // Test di creazione e salvataggio
            testCreateAndSave();
            
            // Test di ricerca
            testSearch();
            
            // Test di aggiornamento
            testUpdate();
            
            // Test di eliminazione
            testDelete();
            
            LOGGER.info("Tutti i test completati con successo");
        } catch (Exception e) {
            LOGGER.severe("Errore durante l'esecuzione dei test: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test di creazione e salvataggio dei media.
     */
    private void testCreateAndSave() throws LibraryException {
        LOGGER.info("Test di creazione e salvataggio");
        
        // Creazione e salvataggio di un libro
        Media book = MediaFactory.createBook(
                "Il Nome della Rosa", 
                "Umberto Eco", 
                LocalDate.of(1980, 10, 15), 
                "Bompiani", 
                512);
        
        mediaService.saveMedia(book);
        LOGGER.info("Libro creato e salvato: " + book.getDetails());
        
        // Creazione e salvataggio di una rivista
        Media magazine = MediaFactory.createMagazine(
                "Focus", 
                LocalDate.of(2023, 5, 10), 
                "Mondadori", 
                350);
        
        mediaService.saveMedia(magazine);
        LOGGER.info("Rivista creata e salvata: " + magazine.getDetails());
        
        // Verifica che i media siano stati salvati
        List<Media> allMedia = mediaService.findAllMedia();
        LOGGER.info("Numero di media salvati: " + allMedia.size());
        
        if (allMedia.size() < 2) {
            throw new LibraryException("Errore nel salvataggio dei media");
        }
    }
    
    /**
     * Test di ricerca dei media.
     */
    private void testSearch() throws LibraryException {
        LOGGER.info("Test di ricerca");
        
        // Ricerca per titolo
        List<Media> mediaByTitle = mediaService.findMediaByTitle("Rosa");
        LOGGER.info("Media trovati per titolo 'Rosa': " + mediaByTitle.size());
        
        // Ricerca per autore
        List<Book> booksByAuthor = mediaService.findBooksByAuthor("Eco");
        LOGGER.info("Libri trovati per autore 'Eco': " + booksByAuthor.size());
        
        // Ricerca per anno
        List<Media> mediaByYear = mediaService.findMediaByPublicationYear(1980);
        LOGGER.info("Media trovati per anno 1980: " + mediaByYear.size());
    }
    
    /**
     * Test di aggiornamento dei media.
     */
    private void testUpdate() throws LibraryException, MediaNotFoundException {
        LOGGER.info("Test di aggiornamento");
        
        // Trova un libro da aggiornare
        List<Media> books = mediaService.findMediaByTitle("Rosa");
        if (!books.isEmpty()) {
            Media book = books.get(0);
            book.setAvailable(false);
            mediaService.updateMedia(book);
            LOGGER.info("Libro aggiornato: " + book.getDetails());
            
            // Verifica che il libro sia stato aggiornato
            Media updatedBook = mediaService.findMediaById(book.getId());
            if (updatedBook.isAvailable()) {
                throw new LibraryException("Errore nell'aggiornamento del libro");
            }
            LOGGER.info("Verifica aggiornamento completata con successo");
        }
    }
    
    /**
     * Test di eliminazione dei media.
     */
    private void testDelete() throws LibraryException, MediaNotFoundException {
        LOGGER.info("Test di eliminazione");
        
        // Crea un nuovo media da eliminare
        Media mediaToDelete = MediaFactory.createBook(
                "Libro da eliminare", 
                "Autore Test", 
                LocalDate.now(), 
                "Editore Test", 
                100);
        
        mediaService.saveMedia(mediaToDelete);
        LOGGER.info("Media creato per il test di eliminazione: " + mediaToDelete.getDetails());
        
        // Elimina il media
        mediaService.deleteMedia(mediaToDelete.getId());
        LOGGER.info("Media eliminato");
        
        // Verifica che il media sia stato eliminato
        try {
            mediaService.findMediaById(mediaToDelete.getId());
            throw new LibraryException("Errore nell'eliminazione del media");
        } catch (MediaNotFoundException e) {
            LOGGER.info("Verifica eliminazione completata con successo");
        }
    }
    
    /**
     * Metodo main per eseguire i test.
     */
    public static void main(String[] args) {
        MediaServiceTest test = new MediaServiceTest();
        test.runTests();
    }
}