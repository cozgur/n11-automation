package resources.config.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class jsonParser {

    public static JsonObject main(String jsonFile) {

        String projectDir = System.getProperty("user.dir");

        JsonParser jsonParser = new JsonParser();
        Object object = null;

        try {
            object = jsonParser.parse(new FileReader(projectDir + "/src/test/java/resources/dictionary/" + jsonFile + ".json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JsonObject jsonObject = (JsonObject) object;
        assert jsonObject != null;

        return jsonObject;

    }

}
