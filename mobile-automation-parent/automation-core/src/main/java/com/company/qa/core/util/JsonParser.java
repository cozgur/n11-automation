package com.company.qa.core.util;

import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonParser {

    /**
     * Parses a JSON file from the classpath (src/main/resources or src/test/resources).
     * Falls back to the legacy file-system path for backwards compatibility.
     */
    public static JsonObject parse(String jsonFile) {
        // Try classpath first
        InputStream is = JsonParser.class.getClassLoader()
                .getResourceAsStream("dictionary/" + jsonFile + ".json");
        if (is != null) {
            com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
            return parser.parse(new InputStreamReader(is)).getAsJsonObject();
        }

        // Fallback: file-system relative to project dir
        String projectDir = System.getProperty("user.dir");
        com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
        try {
            Object obj = parser.parse(new FileReader(
                    projectDir + "/src/test/java/resources/dictionary/" + jsonFile + ".json"));
            return (JsonObject) obj;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("JSON file not found: " + jsonFile + ".json", e);
        }
    }
}
