package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

// Class for loading configurations from external files
public class ConfigLoader {
    private static final Logger LOGGER = LoggerManager.getLogger(ConfigLoader.class.getName());
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties;

    // Private constructor to avoid instantiation
    private ConfigLoader() {
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
            LOGGER.info("Configurations successfully loaded");
        } catch (IOException e) {
            LOGGER.warning("Unable to load configuration file: " + e.getMessage());
            LOGGER.info("Using default configurations");
        }
    }
}