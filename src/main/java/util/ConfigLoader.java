package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Classe per il caricamento delle configurazioni da file esterni.
 * Implementa la gestione sicura dei segreti come parte della programmazione sicura.
 */
public class ConfigLoader {
    private static final Logger LOGGER = LoggerManager.getLogger(ConfigLoader.class.getName());
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties;

    private ConfigLoader() {
        // Costruttore privato per evitare l'istanziazione
    }

    public static synchronized Properties getProperties() {
        if (properties == null) {
            loadProperties();
        }
        return properties;
    }

    public static String getProperty(String key) {
        return getProperties().getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return getProperties().getProperty(key, defaultValue);
    }

    private static void loadProperties() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
            LOGGER.info("Configurazioni caricate con successo");
        } catch (IOException e) {
            LOGGER.warning("Impossibile caricare il file di configurazione: " + e.getMessage());
            LOGGER.info("Utilizzo delle configurazioni predefinite");
        }
    }
}