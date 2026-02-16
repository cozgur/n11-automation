package com.company.qa.core.config;

import com.company.qa.core.exception.ConfigurationException;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * Utility class for loading and querying YAML configuration files.
 *
 * <p>Configuration files are loaded from the classpath (e.g. {@code src/main/resources}
 * or {@code src/test/resources}). Nested values can be accessed using dot notation.</p>
 *
 * <p><b>Usage:</b></p>
 * <pre>{@code
 * Map<String, Object> config = ConfigReader.load("config/default.yaml");
 * String url = ConfigReader.getString(config, "appium.url");
 * }</pre>
 */
public class ConfigReader {

    /**
     * Loads a YAML configuration file from the classpath.
     *
     * @param resourcePath the classpath-relative path to the YAML file
     *                     (e.g. {@code "config/default.yaml"})
     * @return a map representing the YAML document's top-level keys and values
     * @throws ConfigurationException if the file is not found on the classpath
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> load(String resourcePath) {
        Yaml yaml = new Yaml();
        InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) {
            throw new ConfigurationException("Config file not found on classpath: " + resourcePath);
        }
        return yaml.load(is);
    }

    /**
     * Retrieves a string value from the configuration map.
     *
     * <p>Supports dot notation for nested keys. For example, {@code "appium.url"}
     * navigates into the {@code "appium"} sub-map and retrieves the {@code "url"} key.</p>
     *
     * @param config the configuration map to query
     * @param key    the dot-separated key path
     * @return the value as a string, or {@code null} if the key is not found
     */
    @SuppressWarnings("unchecked")
    public static String getString(Map<String, Object> config, String key) {
        Object value = resolveNestedKey(config, key);
        return value != null ? value.toString() : null;
    }

    /**
     * Retrieves a string value from the configuration map with a fallback default.
     *
     * @param config       the configuration map to query
     * @param key          the dot-separated key path
     * @param defaultValue the value to return if the key is not found
     * @return the value as a string, or {@code defaultValue} if the key is not found
     */
    @SuppressWarnings("unchecked")
    public static String getString(Map<String, Object> config, String key, String defaultValue) {
        Object value = resolveNestedKey(config, key);
        return value != null ? value.toString() : defaultValue;
    }

    /**
     * Retrieves a nested map (section) from the configuration map.
     *
     * @param config the configuration map to query
     * @param key    the dot-separated key path pointing to a nested map
     * @return the nested map, or {@code null} if the key is not found or the
     *         value is not a map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getSection(Map<String, Object> config, String key) {
        Object value = resolveNestedKey(config, key);
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        }
        return null;
    }

    /**
     * Resolves a dot-separated key path against a nested map structure.
     *
     * @param config the root configuration map
     * @param key    the dot-separated key path
     * @return the resolved value, or {@code null} if any segment is missing
     */
    @SuppressWarnings("unchecked")
    private static Object resolveNestedKey(Map<String, Object> config, String key) {
        if (config == null || key == null) {
            return null;
        }

        String[] parts = key.split("\\.");
        Object current = config;

        for (String part : parts) {
            if (current instanceof Map) {
                current = ((Map<String, Object>) current).get(part);
            } else {
                return null;
            }
        }

        return current;
    }
}
