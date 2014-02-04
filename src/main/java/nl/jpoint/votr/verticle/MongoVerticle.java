package nl.jpoint.votr.verticle;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

import java.net.UnknownHostException;

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
        });

    }


    private void insertMockQuestions(DB db) {
        DBCollection questionsCollection = db.getCollection("questions");

        // TODO: implement.
    }

}

