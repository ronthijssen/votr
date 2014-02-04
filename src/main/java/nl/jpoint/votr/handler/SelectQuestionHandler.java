package nl.jpoint.votr.handler;

import nl.jpoint.votr.service.QuestionService;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Container;

public class SelectQuestionHandler {

    private final Logger          log;
    private final QuestionService questionService;

    public SelectQuestionHandler(final Vertx vertx, final Container container) {
        this.log = container.logger();
        this.questionService = new QuestionService(vertx, container);
    }

    public void handle(final HttpServerRequest httpRequest) {
        final String talkId = httpRequest.params().get("talkId");
        final String questionId = httpRequest.params().get("questionId");
        log.info("API: Setting active question for talk " + talkId + " to " + questionId);

        questionService.setActiveQuestion(talkId, Integer.valueOf(questionId));
        JsonObject response = new JsonObject();
        response.putString("status","ok");
        httpRequest.response().end(response.encode());
    }

}