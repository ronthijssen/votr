package nl.jpoint.votr.verticle;

import nl.jpoint.votr.handler.AnswerGetHandler;
import nl.jpoint.votr.handler.AnswerPostHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

public class MainVerticle extends Verticle {

    public static final int SERVER_PORT = 8080;
    public static final String DB_NAME = "testdb";

    public void start() {

        final Logger log = container.logger();

//        MongoClient mongoClient = null;
//        try {
//            mongoClient = new MongoClient( "localhost" , 27017 );
//        } catch (UnknownHostException ex) {
//            log.error("Error connecting to MongoDB: ", ex);
//            return;
//        }
//
//        mongoClient.dropDatabase(DB_NAME);
//        final DB db = mongoClient.getDB(DB_NAME);

        RouteMatcher routeMatcher = new RouteMatcher();

        routeMatcher.get("/api/question/1", new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                new AnswerGetHandler(container).handle(req);
            }
        });

        routeMatcher.post("/api/answer/1", new Handler<HttpServerRequest>() {
            public void handle(final HttpServerRequest req) {
                req.bodyHandler(new AnswerPostHandler(container, req));
            }
        });

        routeMatcher.noMatch(new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                String file = "";
                if (req.path().equals("/")) {
                    file = "index.html";
                } else if (!req.path().contains("..")) {
                    file = req.path();
                }
                req.response().sendFile("web/" + file);
            }
        });

        HttpServer server = vertx.createHttpServer();
        server.requestHandler(routeMatcher);
        server.listen(SERVER_PORT);

    }



}
