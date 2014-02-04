package nl.jpoint.votr.handler;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import nl.jpoint.votr.model.Question;
import nl.jpoint.votr.model.QuestionStatus;
import nl.jpoint.votr.service.QuestionService;
import nl.jpoint.votr.verticle.MongoVerticle;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Container;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

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

        Question activeQuestion = questionService.getActiveQuestion(talkId);
        if (activeQuestion == null) {
            response.putString("status", QuestionStatus.WAITING.name());
            httpRequest.response().end(response.encode());
            return;
        }
        response.putObject("question", activeQuestion.asJsonObject());


        Map<String, Integer> answerCounts = new HashMap<>();

        // For all answers, create a key in the map.
        JsonArray questionOptions = activeQuestion.asJsonObject().getArray("options");
        for (Object optionObj : questionOptions) {
            JsonObject questionOption = (JsonObject) optionObj;
            answerCounts.put(questionOption.getInteger("id").toString(), Integer.valueOf(0));
        }

        // testcode -> to be refactored.
        MongoClient mongoClient;
        try {
            mongoClient = new MongoClient(MongoVerticle.DB_HOST, MongoVerticle.DB_PORT);
        } catch (UnknownHostException ex) {
            log.error("Error connecting to MongoDB: ", ex);
            return;
        }

        final DB db = mongoClient.getDB(MongoVerticle.DB_NAME);
        DBCollection answersColl = db.getCollection("answers");
        DBObject query = new BasicDBObject();
        query.put("talkId", talkId);
        query.put("questionId", activeQuestion.asJsonObject().getInteger("id"));
        DBCursor cursor = answersColl.find(query);
        while (cursor.hasNext()) {
            DBObject currentAnswer = cursor.next();
            String optionId = currentAnswer.get("optionId").toString();
            answerCounts.put(optionId, answerCounts.get(optionId)+1);
        }

        // Convert to Json.
        JsonArray answers = new JsonArray();
        for (String optionId : answerCounts.keySet()) {
            JsonObject answer = new JsonObject();
            answer.putString("id", optionId);
            answer.putString("result", Integer.toString(answerCounts.get(optionId)));
            answers.add(answer);
        }
        response.putArray("answers", answers);

        httpRequest.response().end(response.encode());
    }

}
