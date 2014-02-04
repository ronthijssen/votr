package nl.jpoint.votr.service;

import nl.jpoint.votr.model.Answer;
import nl.jpoint.votr.verticle.MongoVerticle;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Container;

public class AnswerService {

    private Logger log;
    private Vertx  vertx;

    public AnswerService(Vertx vertx, Container container) {
        this.vertx = vertx;
        this.log = container.logger();
    }

    public void saveAnswer(Answer answer) {
        log.info(String.format("Saving answer for talk '%s' and question '%d'. Answer value: %d", answer.getTalkId(),
            answer.getQuestionId(), answer.getOptionId()));

        // fire-and-forget.
        JsonObject query = new JsonObject();
        query.putString("action", "saveAnswer");
        query.putObject("data", answer.asJsonObject());
        vertx.eventBus().send(MongoVerticle.MONGO_VERTICLE_BUS_ADDRESS, query);
    }

}
