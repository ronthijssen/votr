package nl.jpoint.votr.verticle;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import nl.jpoint.votr.model.Question;
import nl.jpoint.votr.service.QuestionService;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MongoVerticle extends Verticle {

    public static final String MONGO_VERTICLE_BUS_ADDRESS = "mongo.verticle.address";
    public static final String DB_NAME = "testdb";
    public static final String DB_HOST = "localhost";
    public static final int DB_PORT = 27017;

    private MongoClient mongoClient;

    public void start() {
        final Logger log = container.logger();

        try {
            mongoClient = new MongoClient(DB_HOST, DB_PORT);
        } catch (UnknownHostException ex) {
            log.error("Error connecting to MongoDB: ", ex);
            return;
        }

        mongoClient.dropDatabase(DB_NAME);
        final DB db = mongoClient.getDB(DB_NAME);

        insertMockQuestions(db);

        EventBus eb = vertx.eventBus();
        eb.registerHandler(MONGO_VERTICLE_BUS_ADDRESS, new Handler<Message>() {

            public void handle(Message message) {
                JsonObject query = (JsonObject) message.body();
                String action = query.getString("action");
                JsonObject data = query.getObject("data");
                switch (action) {
                    case "saveAnswer": saveAnswer(data); break;
                    case "setActiveQuestion": setActiveQuestion(data); break;
                    default: break;
                }
            }

            private void saveAnswer(JsonObject data) {
                log.info("Saving answer: " + data.encode());
                DBCollection answers = db.getCollection("answers");
                DBObject answerData = new BasicDBObject();
                answerData.putAll(data.toMap());
                answers.insert(answerData);
            }

            private void setActiveQuestion(JsonObject data) {
                String talkId = data.getString("talkId");
                Integer questionId = Integer.valueOf(data.getString("questionId"));
                DBCollection questions = db.getCollection("questions");
                DBObject query = new BasicDBObject();
                query.put("talkId", talkId);
                query.put("id", questionId);
                DBObject questionData = questions.findOne(query);

                JsonObject questionObj = new JsonObject();
                questionObj.putObject(talkId, new JsonObject(questionData.toString()));

                vertx.eventBus().send(StateVerticle.UPDATE_ACTIVE_QUESTION_BUS_ADDRESS, questionObj);
            }
        });

    }


    private void insertMockQuestions(DB db) {
        DBCollection questionsCollection = db.getCollection("questions");

        Map<String, List<Question>> allQuestions = new HashMap<>();

        List<Question> devoxxQuestions = new ArrayList<>();
        devoxxQuestions.add(new Question(1L, "Which talk did you like the most?", "None", "Keynote", "Dart", "VertX"));
        devoxxQuestions.add(new Question(2L, "Which Devoxx is the best?", "Belgium", "UK", "France"));
        allQuestions.put("devoxx", devoxxQuestions);

        List<Question> jpointQuestions = new ArrayList<>();
        jpointQuestions.add(new Question(1L, "Favorite coffee?", "Americano", "Espresso", "Cappucino", "Latte"));
        jpointQuestions.add(new Question(2L, "Best day of the week?", "Monday", "Tuesday", "Wednesday"));
        allQuestions.put("JPoint", jpointQuestions);

        for (String talkId : allQuestions.keySet()) {
            List<Question> questionsForTalk = allQuestions.get(talkId);
            for (Question question : questionsForTalk) {
                DBObject dbObject = new BasicDBObject();
                dbObject.putAll(question.asJsonObject().toMap());
                dbObject.put("talkId", talkId);
                questionsCollection.insert(dbObject);
            }
        }

        // Set initial talk statuses.
        QuestionService questionService = new QuestionService(vertx, container);

        for (String talkId : allQuestions.keySet()) {
            questionService.clearActiveQuestion(talkId);
        }

    }

}

