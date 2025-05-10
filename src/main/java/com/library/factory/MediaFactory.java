package com.library.factory;

import com.library.model.media.Book;
import com.library.model.media.Magazine;
import com.library.model.media.Media;
import com.library.model.media.MediaCollection;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Factory per la creazione di oggetti Media.
 * Implementa il pattern Factory.
 */
public class MediaFactory {
    
    public static Media createBook(String title, String author, String isbn, 
                                  LocalDate publicationDate, String publisher, int pages) {
        String id = generateId();
        return new Book(id, title, author, isbn, publicationDate, publisher, pages);
    }
    
    public static Media createMagazine(String title, String issn, LocalDate publicationDate, 
                                      String publisher, int issue) {
        String id = generateId();
        return new Magazine(id, title, issn, publicationDate, publisher, issue);
    }
    
    public static MediaCollection createMediaCollection(String title) {
        String id = generateId();
        return new MediaCollection(id, title, LocalDate.now());
    }
    
    private static String generateId() {
        return UUID.randomUUID().toString();
    }
}