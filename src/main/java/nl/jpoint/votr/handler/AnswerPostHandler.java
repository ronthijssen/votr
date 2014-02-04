package nl.jpoint.votr.handler;

import nl.jpoint.votr.model.Answer;
import nl.jpoint.votr.service.AnswerService;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Container;

public class AnswerPostHandler implements Handler<Buffer> {

    private final Logger            log;
    private final HttpServerRequest httpRequest;
    private final AnswerService     answerService;

    public AnswerPostHandler(final Vertx vertx, final Container container, final HttpServerRequest request) {
        this.log = container.logger();
        this.httpRequest = request;
        this.answerService = new AnswerService(vertx, container);
    }

    @Override
    public void handle(Buffer event) {
        JsonObject request = new JsonObject(event.toString());
        log.info("request:" + request);

        storeAnswer(request);
        generateAndSendResponse(request);
    }

    private void storeAnswer(final JsonObject request) {
        int optionId = request.getInteger("optionId");
        Answer answer =
            new Answer(httpRequest.params().get("talkId"), Long.parseLong(httpRequest.params().get("questionId")),
                optionId);
        answerService.saveAnswer(answer);
    }

    private void generateAndSendResponse(final JsonObject request) {
        JsonObject response = new JsonObject();
        response.putString("status", "ok");
        response.putObject("request", request);
        httpRequest.response().end(response.encode());
    }
}
