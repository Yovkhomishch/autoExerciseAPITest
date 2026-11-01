package com.project.autoExerciseAPI.config;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();
    private static final String CONFIG_FILE = "config.properties";

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException("Configuration file not found in resources: " + CONFIG_FILE);
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load configuration file: " + CONFIG_FILE, e);
        }

        getRequiredProperty("base.url");
    }

    private ConfigReader() {
    }

    public static String getProperty(String key, String defaultValue) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }
        return PROPERTIES.getProperty(key, defaultValue);
    }

    public static String getProperty(String key) {
        return getProperty(key, null);
    }

    public static String getRequiredProperty(String key) {
        String value = getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    String.format("Required configuration property '%s' is missing or empty in %s (or System Properties)", key, CONFIG_FILE)
            );
        }
        return value;
    }

    public static int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    String.format("Invalid integer value '%s' for configuration key '%s' in %s", value, key, CONFIG_FILE), e
            );
        }
    }

    public static int getRequiredIntProperty(String key) {
        String value = getRequiredProperty(key);
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    String.format("Invalid integer value '%s' for required configuration key '%s' in %s", value, key, CONFIG_FILE), e
            );
        }
    }
}