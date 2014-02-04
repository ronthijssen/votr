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

    private static int POLLER_COUNTER = 0;

    private final Map<String, Question> questions = new HashMap<>();
    private final Logger log;

    public RequestGetHandler(Container container) {
        this.log = container.logger();

        questions.put("devoxx",
            new Question(1L, "devoxx", "Which talk did you like the most.", "Geen", "Keynote", "Dart", "VertX"));
        questions.put("JPoint",
            new Question(2L, "JPoint", "Favorite coffee?", "Americano", "Espresso", "Cappucino", "Latte"));
        questions.put("poller", null);
    }

    /**
     * Handles the incoming HTTP Request, which represents a query for the current question for given talkId.
     *
     * @param httpRequest the HTTP request.
     */
    public void handle(HttpServerRequest httpRequest) {
        final String talkId = httpRequest.params().get("talkId");
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
        final Question question = currentQuestionForTalk(talkId);
        final JsonObject responseObject;

        if (question != null) {
            responseObject = buildResponseForQuestion(question);
        } else {
            // Hardcoded hack to temporarily support polling
            if (POLLER_COUNTER < 5) {
                POLLER_COUNTER++;
                responseObject = buildResponseForQuestion(null);
            } else {
                if (POLLER_COUNTER++ > 9) {
                    POLLER_COUNTER = 0;
                }
                responseObject = buildResponseForQuestion(currentQuestionForTalk("devoxx"));
            }
        }
        response.end(responseObject.encode());
    }

    private Question currentQuestionForTalk(final String talkId) {
        return questions.get(talkId);
    }

    private void respondForUnknownTalk(final HttpServerResponse response) {
        response.setStatusCode(404).end();
    }

    private JsonObject buildResponseForQuestion(final Question question) {
        JsonObject response = new JsonObject();
        if (question == null) {
            response.putString("status", QuestionStatus.WAITING.name());
        } else {
            response.putString("status", QuestionStatus.QUESTION.name());
            response.putObject("question", question.asJsonObject());
        }
        return response;
    }
}
