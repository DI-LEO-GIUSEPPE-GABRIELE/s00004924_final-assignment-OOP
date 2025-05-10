package com.library.repository;

import com.library.exception.LibraryException;
import com.library.model.media.Media;
import com.library.util.LoggerManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Classe per la gestione della persistenza dei dati su file.
 * Implementa le operazioni di salvataggio e caricamento dei media.
 */
public class FileStorageManager {
    private static final Logger LOGGER = LoggerManager.getLogger(FileStorageManager.class.getName());
    private static final String DATA_DIRECTORY = "data";
    private static final String MEDIA_FILE = DATA_DIRECTORY + File.separator + "media.dat";
    private static FileStorageManager instance;
    
    private FileStorageManager() {
        // Crea la directory dei dati se non esiste
        File dataDir = new File(DATA_DIRECTORY);
        if (!dataDir.exists()) {
            if (dataDir.mkdir()) {
                LOGGER.info("Directory dei dati creata: " + dataDir.getAbsolutePath());
            } else {
                LOGGER.warning("Impossibile creare la directory dei dati: " + dataDir.getAbsolutePath());
            }
        }
    }
    
    /**
     * Ottiene l'istanza singleton del gestore di archiviazione.
     * 
     * @return l'istanza del gestore di archiviazione
     */
    public static synchronized FileStorageManager getInstance() {
        if (instance == null) {
            instance = new FileStorageManager();
        }
        return instance;
    }
    
    /**
     * Salva i media su file.
     * 
     * @param mediaMap la mappa dei media da salvare
     * @throws LibraryException se si verifica un errore durante il salvataggio
     */
    public void saveMedia(Map<String, Media> mediaMap) throws LibraryException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MEDIA_FILE))) {
            oos.writeObject(new ArrayList<>(mediaMap.values()));
            LOGGER.info("Media salvati su file: " + MEDIA_FILE);
        } catch (IOException e) {
            LOGGER.severe("Errore durante il salvataggio dei media su file: " + e.getMessage());
            throw new LibraryException("Errore durante il salvataggio dei media: " + e.getMessage());
        }
    }
    
    /**
     * Carica i media da file.
     * 
     * @return la lista dei media caricati
     * @throws LibraryException se si verifica un errore durante il caricamento
     */
    @SuppressWarnings("unchecked")
    public List<Media> loadMedia() throws LibraryException {
        File file = new File(MEDIA_FILE);
        if (!file.exists()) {
            LOGGER.info("File dei media non trovato. Verrà creato un nuovo file al primo salvataggio.");
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Media> mediaList = (List<Media>) ois.readObject();
            LOGGER.info("Media caricati da file: " + MEDIA_FILE + ", numero di media: " + mediaList.size());
            return mediaList;
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.severe("Errore durante il caricamento dei media da file: " + e.getMessage());
            throw new LibraryException("Errore durante il caricamento dei media: " + e.getMessage());
        }
    }
}