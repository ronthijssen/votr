package nl.jpoint.votr.service;

import nl.jpoint.votr.model.Question;
import nl.jpoint.votr.verticle.StateVerticle;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Container;

import java.util.Map;

public class QuestionService {

    private Logger log;
    private Vertx vertx;

    public QuestionService(Vertx vertx, Container container) {
        this.vertx = vertx;
        this.log = container.logger();
    }

    public Question getActiveQuestion(String talkId) {
        final Map<String, String> questions = vertx.sharedData().getMap(StateVerticle.ACTIVE_QUESTION_DATA);

        String activeQuestion = questions.get(talkId);
        if (activeQuestion == null) {
            return null;
        }
        return new Question(new JsonObject(activeQuestion));
    }

}
