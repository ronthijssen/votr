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

        Question currentQuestion = questionService.getActiveQuestion(talkId);
        if (currentQuestion == null) {
            response.putString("status", QuestionStatus.WAITING.name());
            httpRequest.response().end(response.encode());
            return;
        }

        response.putObject("question", currentQuestion.asJsonObject());

        Map<String, Integer> answerCounts = new HashMap<>();


        // mock answers.
        JsonArray options = currentQuestion.asJsonObject().getArray("options");
        for (Object optionObj : options) {
            JsonObject questionOption = (JsonObject) optionObj;
            answerCounts.put(questionOption.getInteger("id").toString(), Integer.valueOf(0));
        }


        // testcode.
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
        query.put("questionId", currentQuestion.asJsonObject().getInteger("id"));
        DBCursor cursor = answersColl.find(query);
        while (cursor.hasNext()) {
            DBObject current = cursor.next();
            String optionId = current.get("optionId").toString();
            log.info("found answer: " + current);
            Integer answerCount = answerCounts.get(optionId);
            answerCounts.put(optionId, answerCount+1);
        }


        JsonArray answers = new JsonArray();
        for (Object optionObj : options) {
            JsonObject questionOption = (JsonObject) optionObj;
            JsonObject answer = new JsonObject();

            answer.putString("id", questionOption.getInteger("id").toString());

            answer.putString("result", Integer.toString(answerCounts.get(questionOption.getInteger("id").toString())));
            answers.add(answer);
        }

        response.putArray("answers", answers);
        httpRequest.response().end(response.encode());


    }

}
