package nl.jpoint.votr.model;

import org.vertx.java.core.json.JsonObject;

/**
 * Created by tim on 04-02-14.
 */
public class Answer {

    private final JsonObject jsonObject = new JsonObject();

    public Answer(final String talkId, final long questionId, final int optionId) {
        jsonObject.putString("talkId", talkId);
        jsonObject.putNumber("questionId", questionId);
        jsonObject.putNumber("optionId", optionId);
    }

    public JsonObject asJsonObject() {
        return jsonObject;
    }

    public static Answer parseJsonObject(final JsonObject answerObject) {
        return new Answer(answerObject.getString("talkId"), answerObject.getLong("questionId"),
            answerObject.getInteger("optionId"));
    }
}
