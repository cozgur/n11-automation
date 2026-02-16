package com.company.qa.core.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class ConfigReader {

    /**
     * Loads a YAML config file from the classpath.
     * Usage: ConfigReader.load("config/default.yaml")
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> load(String resourcePath) {
        Yaml yaml = new Yaml();
        InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) {
            throw new RuntimeException("Config file not found on classpath: " + resourcePath);
        }
        return yaml.load(is);
    }

    /**
     * Gets a string value from the config map.
     * Supports dot notation for nested keys, e.g. "appium.url" navigates into
     * the "appium" sub-map and retrieves the "url" key.
     */
    @SuppressWarnings("unchecked")
    public static String getString(Map<String, Object> config, String key) {
        Object value = resolveNestedKey(config, key);
        return value != null ? value.toString() : null;
    }

    @SuppressWarnings("unchecked")
    public static String getString(Map<String, Object> config, String key, String defaultValue) {
        Object value = resolveNestedKey(config, key);
        return value != null ? value.toString() : defaultValue;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getSection(Map<String, Object> config, String key) {
        Object value = resolveNestedKey(config, key);
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        }
        return null;
    }

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
