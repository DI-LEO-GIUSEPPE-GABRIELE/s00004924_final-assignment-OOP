package com.library.exception;

/**
 * Eccezione lanciata quando un media non viene trovato.
 * Parte del pattern Exception Shielding.
 */
public class MediaNotFoundException extends LibraryException {
    
    public MediaNotFoundException(String mediaId) {
        super("Media con ID " + mediaId + " non trovato");
    }
}