package main.java;
import ui.ConsoleUI;
import util.LoggerManager;
import java.util.logging.Logger;

// Main class of the application
public class Main {
    private static final Logger LOGGER = LoggerManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Starting the Library Management System");
        
        try {
            ConsoleUI ui = new ConsoleUI();
            ui.start();
        } catch (Exception e) {
            LOGGER.severe("Error while running the application: " + e.getMessage());
            System.out.println("An error occurred. See logs for more details.");
        }
        
        LOGGER.info("Closing of the Library Management System");
    }
}