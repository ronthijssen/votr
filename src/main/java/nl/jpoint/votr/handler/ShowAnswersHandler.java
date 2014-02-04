package nl.jpoint.votr.handler;

import nl.jpoint.votr.model.Question;
import nl.jpoint.votr.model.QuestionStatus;
import nl.jpoint.votr.service.QuestionService;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Container;

public class ShowAnswersHandler {

    private final Logger          log;
    private final QuestionService questionService;

    public ShowAnswersHandler(final Vertx vertx, final Container container) {
        this.log = container.logger();
        this.questionService = new QuestionService(vertx, container);
    }

    public void handle(final HttpServerRequest httpRequest) {
        final String talkId = httpRequest.params().get("talkId");
        JsonObject response = new JsonObject();

        Question currentQuestion = questionService.getActiveQuestion(talkId);
        if (currentQuestion == null) {
            response.putString("status", QuestionStatus.WAITING.name());
            httpRequest.response().end(response.encode());
            return;
        }

        response.putObject("question", currentQuestion.asJsonObject());

        // mock answers.
        JsonArray answers = new JsonArray();
        JsonArray options = currentQuestion.asJsonObject().getArray("options");
        for (Object optionObj : options) {
            JsonObject questionOption = (JsonObject) optionObj;
            JsonObject answer = new JsonObject();
            answer.putString("id", questionOption.getInteger("id").toString());

            int result = Integer.valueOf(questionOption.getInteger("id")) * 10;
            answer.putString("result", Integer.toString(result));
            answers.add(answer);
        }

        response.putArray("answers", answers);

        httpRequest.response().end(response.encode());

    }

}
