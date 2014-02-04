package nl.jpoint.votr.verticle;

import com.mongodb.MongoClient;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

public class MongoVerticle extends Verticle {

    private MongoClient mongoClient;
    public static final String MONGO_VERTICLE_BUS_ADDRESS = "mongo.verticle.address";

    public void start() {
        final Logger log = container.logger();

//        try {
//            mongoClient = new MongoClient( "localhost" , 27017 );
//        } catch (UnknownHostException ex) {
//            log.error("Error connecting to MongoDB: ", ex);
//            return;
//        }
//
//        mongoClient.dropDatabase("testdb");
//        final DB db = mongoClient.getDB( "testdb" );

        EventBus eb = vertx.eventBus();
        eb.registerHandler(MONGO_VERTICLE_BUS_ADDRESS, new Handler<Message>() {

            public void handle(Message message) {
//                BasicDBObject doc = new BasicDBObject();
//                for (String key : fields) {
//                    String value = (String)jsonObject.getString(key);
//                    doc.append(key, value);
//                }
//
//                DBCollection coll = db.getCollection("recordedResponses");
//                coll.insert(doc);
                message.reply(new JsonObject().putString("responseFromMongo", "response:ok"));
            }
        });

    }


}

