package com.company.qa.core.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

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
            try (Reader reader = new InputStreamReader(is)) {
                JsonElement element = com.google.gson.JsonParser.parseReader(reader);
                return element.getAsJsonObject();
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse JSON from classpath: " + jsonFile + ".json", e);
            }
        }

        // Fallback: file-system relative to project dir
        String projectDir = System.getProperty("user.dir");
        String filePath = projectDir + "/src/test/java/resources/dictionary/" + jsonFile + ".json";
        try (Reader reader = new FileReader(filePath)) {
            JsonElement element = com.google.gson.JsonParser.parseReader(reader);
            return element.getAsJsonObject();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("JSON file not found: " + jsonFile + ".json", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON from file: " + filePath, e);
        }
    }
}
