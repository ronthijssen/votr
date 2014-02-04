package nl.jpoint.votr.handler;

import nl.jpoint.votr.model.Question;
import nl.jpoint.votr.model.QuestionStatus;
import nl.jpoint.votr.service.QuestionService;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Container;

public class GetQuestionHandler {

    private static int POLLER_COUNTER = 0;

    private final Logger          log;
    private final QuestionService questionService;

    public GetQuestionHandler(final Vertx vertx, final Container container) {
        this.log = container.logger();
        this.questionService = new QuestionService(vertx, container);
    }

    /**
     * Handles the incoming HTTP Request, which represents a query for the current question for given talkId.
     *
     * @param httpRequest the HTTP request.
     */
    public void handle(final HttpServerRequest httpRequest) {
        final String talkId = httpRequest.params().get("talkId");
        if (talkExists(talkId)) {
            respondWithCurrentQuestionForTalk(httpRequest.response(), talkId);
        } else {
            respondForUnknownTalk(httpRequest.response());
        }
    }

    private boolean talkExists(final String talkId) {
        return questionService.isExistingTalk(talkId);
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
        return questionService.getActiveQuestion(talkId);
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
