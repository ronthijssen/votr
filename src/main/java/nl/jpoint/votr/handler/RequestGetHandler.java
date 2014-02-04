package nl.jpoint.votr.handler;

import java.util.HashMap;
import java.util.Map;

import nl.jpoint.votr.model.Question;
import nl.jpoint.votr.model.QuestionStatus;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Container;

public class RequestGetHandler {

    private final Map<String, Question> questions = new HashMap<String, Question>();

    private Logger log;

    public RequestGetHandler(Container container) {
        this.log = container.logger();

        questions.put("devoxx", new Question(1L, "Which talk did you like the most.", "Geen", "Keynote", "Dart", "VertX"));
        questions.put("JPoint", new Question(2L, "Favorite coffee?", "Americano", "Espresso", "Cappucino", "Latte"));
    }

    /**
     * Handles the incoming HTTP Request, which represents a query for the current question for given talkId.
     * @param httpRequest the HTTP request.
     * @param talkId the id of the talk the question is requested for.
     */
    public void handle(HttpServerRequest httpRequest, String talkId) {
        if (talkExists(talkId)) {
            respondWithCurrentQuestionForTalk(httpRequest.response(), talkId);
        } else {
            respondForUnknownTalk(httpRequest.response());
        }
    }

    private boolean talkExists(final String talkId) {
        return questions.containsKey(talkId);
    }

    private void respondWithCurrentQuestionForTalk(final HttpServerResponse response, final String talkId) {
        response.end(currentQuestionForTalk(talkId).asObject().encode());
    }

    private JsonObject currentQuestionForTalk(final String talkId) {
        return questions.get(talkId).asJsonObject();
    }

    private void respondForUnknownTalk(final HttpServerResponse response) {
        response.setStatusCode(404).end();
    }


}
