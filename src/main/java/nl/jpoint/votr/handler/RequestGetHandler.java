package nl.jpoint.votr.handler;

import java.util.HashMap;
import java.util.Map;

import nl.jpoint.votr.model.Question;
import nl.jpoint.votr.model.QuestionStatus;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Container;

public class RequestGetHandler {

    private final Map<String, Question> questions = new HashMap<String, Question>();

    private Logger log;

    public RequestGetHandler(Container container) {
        this.log = container.logger();

        questions.put("devoxx", new Question("Which talk did you like the most.", "Geen", "Keynote", "Dart", "VertX"));
        questions.put("JPoint", new Question("Favorite coffee?", "Americano", "Espresso", "Cappucino", "Latte"));
    }

    public void handle(HttpServerRequest httpRequest, String talkId) {
        final JsonObject response;
        if (questions.containsKey(talkId)) {
            response = questions.get(talkId).asJsonObject();
        } else {
            response = buildNoAvailableQuestion();
        }
        httpRequest.response().end(response.encode());
    }

    private JsonObject buildNoAvailableQuestion() {
        return new JsonObject().putString("status", QuestionStatus.WAITING.name());
    }
}
