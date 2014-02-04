package nl.jpoint.votr.service;

import nl.jpoint.votr.verticle.MongoVerticle;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Container;

public class MongoService {

    private Logger log;
    private Vertx vertx;

    public MongoService(Vertx vertx, Container container) {
        this.vertx = vertx;
        this.log = container.logger();
    }

    public void doTest(HttpServerRequest httpRequest) {
        JsonObject query = new JsonObject();
        query.putString("collection", "testcollection");
        vertx.eventBus().send(MongoVerticle.MONGO_VERTICLE_BUS_ADDRESS, query, new MongoResponseHandler(httpRequest));
    }


    private class MongoResponseHandler implements Handler<Message<JsonObject>> {

        private HttpServerRequest httpRequest;

        public MongoResponseHandler(HttpServerRequest httpRequest) {
            this.httpRequest = httpRequest;
        }

        @Override
        public void handle(Message<JsonObject> event) {
            log.info("Received from Mongo: " + event.body());

//            JsonObject response = new JsonObject();
//            response.putString("result", "ok");
            httpRequest.response().end(event.body().toString());
        }
    }
}
