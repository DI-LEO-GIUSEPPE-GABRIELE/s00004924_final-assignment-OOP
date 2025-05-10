package com.library;

import com.library.ui.ConsoleUI;
import com.library.util.LoggerManager;

import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = LoggerManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Avvio del Sistema di Gestione Biblioteca");
        
        try {
            ConsoleUI ui = new ConsoleUI();
            ui.start();
        } catch (Exception e) {
            LOGGER.severe("Errore durante l'esecuzione dell'applicazione: " + e.getMessage());
            System.out.println("Si è verificato un errore. Consultare i log per maggiori dettagli.");
        }
        
        LOGGER.info("Chiusura del Sistema di Gestione Biblioteca");
    }
}