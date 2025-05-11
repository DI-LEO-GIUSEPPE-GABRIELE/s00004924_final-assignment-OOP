package service;

import exception.LibraryException;
import exception.MediaNotFoundException;
import factory.MediaFactory;
import model.media.Book;
import model.media.Media;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Test unitari per il servizio MediaService.
 * Verifica il corretto funzionamento delle operazioni CRUD e delle ricerche.
 */
public class MediaServiceJUnitTest {

    private MediaService mediaService;
    private Media testBook;
    private Media testMagazine;

    @Before
    public void setUp() throws LibraryException {
        mediaService = MediaService.getInstance();

        // Crea un libro e una rivista di test
        testBook = MediaFactory.createBook(
                "Test Book",
                "Test Author",
                LocalDate.now(),
                "Test Publisher",
                100);

        testMagazine = MediaFactory.createMagazine(
                "Test Magazine",
                LocalDate.now(),
                "Test Publisher",
                1);

        // Salva i media di test
        mediaService.saveMedia(testBook);
        mediaService.saveMedia(testMagazine);
    }

    @Test
    public void testSaveAndFindById() throws LibraryException, MediaNotFoundException {
        // Verifica che i media siano stati salvati correttamente
        Media foundBook = mediaService.findMediaById(testBook.getId());
        Media foundMagazine = mediaService.findMediaById(testMagazine.getId());

        assertNotNull("Il libro non è stato trovato", foundBook);
        assertNotNull("La rivista non è stata trovata", foundMagazine);
        assertEquals("L'ID del libro non corrisponde", testBook.getId(), foundBook.getId());
        assertEquals("L'ID della rivista non corrisponde", testMagazine.getId(), foundMagazine.getId());
    }

    @Test
    public void testFindAll() throws LibraryException {
        // Verifica che findAll restituisca una lista non vuota
        List<Media> allMedia = mediaService.findAllMedia();
        assertNotNull("La lista dei media è nulla", allMedia);
        assertFalse("La lista dei media è vuota", allMedia.isEmpty());
    }

    @Test
    public void testFindByTitle() {
        // Verifica la ricerca per titolo
        List<Media> mediaByTitle = mediaService.findMediaByTitle("Test");
        assertNotNull("La lista dei media è nulla", mediaByTitle);
        assertFalse("La lista dei media è vuota", mediaByTitle.isEmpty());
    }

    @Test
    public void testFindByAuthor() {
        // Verifica la ricerca per autore
        List<Book> booksByAuthor = mediaService.findBooksByAuthor("Test Author");
        assertNotNull("La lista dei libri è nulla", booksByAuthor);
        assertFalse("La lista dei libri è vuota", booksByAuthor.isEmpty());
    }

    @Test
    public void testUpdate() throws LibraryException, MediaNotFoundException {
        // Verifica l'aggiornamento di un media
        testBook.setAvailable(false);
        Media updatedBook = mediaService.updateMedia(testBook);

        assertNotNull("Il libro aggiornato è nullo", updatedBook);
        assertFalse("Lo stato di disponibilità non è stato aggiornato", updatedBook.isAvailable());

        // Verifica che l'aggiornamento sia persistente
        Media retrievedBook = mediaService.findMediaById(testBook.getId());
        assertFalse("Lo stato di disponibilità non è persistente", retrievedBook.isAvailable());
    }

    @Test(expected = MediaNotFoundException.class)
    public void testDelete() throws LibraryException, MediaNotFoundException {
        // Crea un nuovo media da eliminare
        Media mediaToDelete = MediaFactory.createBook(
                "Delete Test Book",
                "Delete Test Author",
                LocalDate.now(),
                "Delete Test Publisher",
                200);

        mediaService.saveMedia(mediaToDelete);

        // Verifica che il media sia stato creato
        Media foundMedia = mediaService.findMediaById(mediaToDelete.getId());
        assertNotNull("Il media non è stato creato", foundMedia);

        // Elimina il media
        mediaService.deleteMedia(mediaToDelete.getId());

        // Questo dovrebbe lanciare MediaNotFoundException
        mediaService.findMediaById(mediaToDelete.getId());
    }
}