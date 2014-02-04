package nl.jpoint.votr.model;

import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

/**
 * Object representing a question and corresponding options.
 */
public class Question {

    private final JsonObject jsonObject = new JsonObject();

    public Question(final String title, final String... options) {
        jsonObject.putString("status", QuestionStatus.QUESTION.name());
        jsonObject.putString("title", title);
        jsonObject.putArray("options", optionsToJsonArray(options));
    }

    private JsonArray optionsToJsonArray(final String[] options) {
        JsonArray jsonArray = new JsonArray(options);
        return jsonArray;
    }

    public JsonObject asJsonObject() {
        return jsonObject;
    }
}
