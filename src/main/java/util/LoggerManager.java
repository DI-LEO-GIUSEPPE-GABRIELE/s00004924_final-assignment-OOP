package util;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.*;

// Class for logging management
public class LoggerManager {
    private static final String LOG_FILE = "library_system.log";
    private static boolean initialized = false;

    // Private constructor to avoid instantiation
    private LoggerManager() {
    }

    public static synchronized Logger getLogger(String name) {
        if (!initialized) {
            initializeLogging();
        }
        return Logger.getLogger(name);
    }

    private static void initializeLogging() {
        try {
            // Set english language for log messages
            Locale.setDefault(Locale.ENGLISH);

            // Logger configuration
            Logger rootLogger = Logger.getLogger("");

            // Remove existing handlers
            for (Handler handler : rootLogger.getHandlers()) {
                rootLogger.removeHandler(handler);
            }

            // Add a file handler
            FileHandler fileHandler = new FileHandler(LOG_FILE, true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
            rootLogger.addHandler(fileHandler);

            // Add a console handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            consoleHandler.setLevel(Level.INFO);
            rootLogger.addHandler(consoleHandler);

            // Set the logger level
            rootLogger.setLevel(Level.ALL);

            initialized = true;
        } catch (IOException e) {
            System.err.println("Unable to initialize logging: " + e.getMessage());
            e.printStackTrace();
        }
    }
}