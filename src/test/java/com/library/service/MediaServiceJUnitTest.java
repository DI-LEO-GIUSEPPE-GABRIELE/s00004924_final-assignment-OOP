package com.library.service;

import com.library.exception.LibraryException;
import com.library.exception.MediaNotFoundException;
import com.library.factory.MediaFactory;
import com.library.model.media.Book;
import com.library.model.media.Media;
import com.library.model.media.MediaCollection;
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
                "978-1234567890", 
                LocalDate.now(), 
                "Test Publisher", 
                100);
        
        testMagazine = MediaFactory.createMagazine(
                "Test Magazine", 
                "1234-5678", 
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
    
    @Test
    public void testMediaCollection() throws LibraryException, MediaNotFoundException {
        // Crea una collezione
        MediaCollection collection = MediaFactory.createMediaCollection("Test Collection");
        mediaService.saveMedia(collection);
        
        // Aggiungi un media alla collezione
        mediaService.addMediaToCollection(collection.getId(), testBook.getId());
        
        // Verifica che il media sia stato aggiunto alla collezione
        Media retrievedCollection = mediaService.findMediaById(collection.getId());
        assertTrue("L'oggetto recuperato non è una collezione", retrievedCollection instanceof MediaCollection);
        
        MediaCollection actualCollection = (MediaCollection) retrievedCollection;
        assertFalse("La collezione non contiene media", actualCollection.getMediaItems().isEmpty());
        assertEquals("La collezione non contiene il numero corretto di media", 1, actualCollection.getMediaItems().size());
    }
    
    @Test(expected = MediaNotFoundException.class)
    public void testDelete() throws LibraryException, MediaNotFoundException {
        // Crea un nuovo media da eliminare
        Media mediaToDelete = MediaFactory.createBook(
                "Delete Test Book", 
                "Delete Test Author", 
                "978-0987654321", 
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