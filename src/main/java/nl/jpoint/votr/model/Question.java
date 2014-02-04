package nl.jpoint.votr.model;

import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

/**
 * Object representing a question and corresponding options.
 */
public class Question {

    private JsonObject jsonObject = new JsonObject();

    public Question(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Question(final long id, final String talkId, final String title, final String... options) {
        jsonObject.putNumber("id", id);
        jsonObject.putString("talkId", talkId);
        jsonObject.putString("title", title);
        jsonObject.putArray("options", optionsToJsonArray(options));
    }

    private JsonArray optionsToJsonArray(final String[] options) {
        JsonArray jsonArray = new JsonArray();
        int i = 0;
        for (String option : options) {
            jsonArray.addObject(buildOptionObject(++i, option));
        }
        return jsonArray;
    }

    private JsonObject buildOptionObject(final int id, final String option) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.putNumber("id", id);
        jsonObject.putString("answer", option);
        return jsonObject;
    }

    public JsonObject asJsonObject() {
        return jsonObject;
    }
}
