package com.library.repository;

import com.library.exception.LibraryException;
import com.library.exception.MediaNotFoundException;

import java.util.List;

/**
 * Interfaccia generica per i repository.
 * Definisce le operazioni CRUD di base.
 * @param <T> il tipo di entità gestita dal repository
 * @param <ID> il tipo dell'identificatore dell'entità
 */
public interface Repository<T, ID> {
    
    /**
     * Salva un'entità nel repository.
     * 
     * @param entity l'entità da salvare
     * @return l'entità salvata
     * @throws LibraryException se si verifica un errore durante il salvataggio
     */
    T save(T entity) throws LibraryException;
    
    /**
     * Trova un'entità per ID.
     * 
     * @param id l'ID dell'entità da trovare
     * @return l'entità trovata
     * @throws MediaNotFoundException se l'entità non viene trovata
     */
    T findById(ID id) throws MediaNotFoundException;
    
    /**
     * Trova tutte le entità nel repository.
     * 
     * @return una lista di tutte le entità
     * @throws LibraryException se si verifica un errore durante la ricerca
     */
    List<T> findAll() throws LibraryException;
    
    /**
     * Elimina un'entità dal repository.
     * 
     * @param id l'ID dell'entità da eliminare
     * @throws MediaNotFoundException se l'entità non viene trovata
     */
    void delete(ID id) throws MediaNotFoundException;
    
    /**
     * Aggiorna un'entità nel repository.
     * 
     * @param entity l'entità da aggiornare
     * @return l'entità aggiornata
     * @throws MediaNotFoundException se l'entità non viene trovata
     */
    T update(T entity) throws MediaNotFoundException;
}