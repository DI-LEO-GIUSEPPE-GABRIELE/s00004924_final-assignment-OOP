package com.library.util;

import java.io.IOException;
import java.util.logging.*;

/**
 * Gestore centralizzato per il logging dell'applicazione.
 */
public class LoggerManager {
    private static final String LOG_FILE = "library_system.log";
    private static boolean initialized = false;

    private LoggerManager() {
        // Costruttore privato per evitare l'istanziazione
    }

    public static synchronized Logger getLogger(String name) {
        if (!initialized) {
            initializeLogging();
        }
        return Logger.getLogger(name);
    }

    private static void initializeLogging() {
        try {
            // Configurazione del logger
            Logger rootLogger = Logger.getLogger("");
            
            // Rimuovi gli handler esistenti
            for (Handler handler : rootLogger.getHandlers()) {
                rootLogger.removeHandler(handler);
            }
            
            // Aggiungi un file handler
            FileHandler fileHandler = new FileHandler(LOG_FILE, true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
            rootLogger.addHandler(fileHandler);
            
            // Aggiungi un console handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            consoleHandler.setLevel(Level.INFO);
            rootLogger.addHandler(consoleHandler);
            
            // Imposta il livello del logger
            rootLogger.setLevel(Level.ALL);
            
            initialized = true;
        } catch (IOException e) {
            System.err.println("Impossibile inizializzare il logging: " + e.getMessage());
            e.printStackTrace();
        }
    }
}