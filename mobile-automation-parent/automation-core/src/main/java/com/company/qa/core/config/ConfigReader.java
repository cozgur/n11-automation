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

    public static String getString(Map<String, Object> config, String key) {
        Object value = config.get(key);
        return value != null ? value.toString() : null;
    }

    public static String getString(Map<String, Object> config, String key, String defaultValue) {
        Object value = config.get(key);
        return value != null ? value.toString() : defaultValue;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getSection(Map<String, Object> config, String key) {
        Object value = config.get(key);
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        }
        return null;
    }
}
