package nl.jpoint.votr.service;

import nl.jpoint.votr.model.Question;
import nl.jpoint.votr.verticle.MongoVerticle;
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

        if (activeQuestion == null || activeQuestion.isEmpty()) {
            return null;
        }
        return new Question(new JsonObject(activeQuestion));
    }

    public boolean isExistingTalk(String talkId) {
        final Map<String, String> questions = vertx.sharedData().getMap(StateVerticle.ACTIVE_QUESTION_DATA);
        return questions.containsKey(talkId);
    }

    public void setActiveQuestion(String talkId, Integer questionId) {
        log.info("Setting active question to talkId " + talkId + ", questionId " + questionId);

        JsonObject data = new JsonObject();
        data.putString("talkId", talkId);
        data.putString("questionId", questionId.toString());

        JsonObject query = new JsonObject();
        query.putString("action", "setActiveQuestion");
        query.putObject("data", data);
        vertx.eventBus().send(MongoVerticle.MONGO_VERTICLE_BUS_ADDRESS, query);
    }


    public void clearActiveQuestion(String talkId) {
        log.info("Clearing active question for talkId " + talkId);

        JsonObject questionObj = new JsonObject();
        questionObj.putObject(talkId, null);

        vertx.eventBus().send(StateVerticle.UPDATE_ACTIVE_QUESTION_BUS_ADDRESS, questionObj);
    }


}
