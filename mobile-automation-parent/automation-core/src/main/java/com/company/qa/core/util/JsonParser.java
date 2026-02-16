package com.company.qa.core.util;

import com.company.qa.core.exception.ConfigurationException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Utility class for parsing JSON configuration files.
 *
 * <p>Looks up JSON files in two locations (in order):</p>
 * <ol>
 *   <li>Classpath: {@code dictionary/<name>.json} (e.g. from {@code src/main/resources}
 *       or {@code src/test/resources})</li>
 *   <li>File system: {@code <project.dir>/src/test/java/resources/dictionary/<name>.json}
 *       (legacy fallback)</li>
 * </ol>
 */
public class JsonParser {

    /**
     * Parses a JSON file and returns its content as a {@link JsonObject}.
     *
     * <p>The {@code jsonFile} parameter is the base name without the {@code .json}
     * extension. For example, passing {@code "apps"} will look for
     * {@code dictionary/apps.json} on the classpath.</p>
     *
     * @param jsonFile the base name of the JSON file (without extension); must not
     *                 be {@code null} or empty
     * @return the parsed {@link JsonObject}
     * @throws ConfigurationException if the file name is {@code null}/empty, the
     *                                file is not found, or parsing fails
     */
    public static JsonObject parse(String jsonFile) {
        if (jsonFile == null || jsonFile.trim().isEmpty()) {
            throw new ConfigurationException("JSON file name must not be null or empty");
        }

        // Try classpath first
        InputStream is = JsonParser.class.getClassLoader()
                .getResourceAsStream("dictionary/" + jsonFile + ".json");
        if (is != null) {
            try (Reader reader = new InputStreamReader(is)) {
                JsonElement element = com.google.gson.JsonParser.parseReader(reader);
                return element.getAsJsonObject();
            } catch (Exception e) {
                throw new ConfigurationException("Failed to parse JSON from classpath: " + jsonFile + ".json", e);
            }
        }

        // Fallback: file-system relative to project dir
        String projectDir = System.getProperty("user.dir");
        String filePath = projectDir + "/src/test/java/resources/dictionary/" + jsonFile + ".json";
        try (Reader reader = new FileReader(filePath)) {
            JsonElement element = com.google.gson.JsonParser.parseReader(reader);
            return element.getAsJsonObject();
        } catch (FileNotFoundException e) {
            throw new ConfigurationException("JSON file not found: " + jsonFile + ".json", e);
        } catch (Exception e) {
            throw new ConfigurationException("Failed to parse JSON from file: " + filePath, e);
        }
    }
}
