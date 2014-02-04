package nl.jpoint.votr;

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

		routeMatcher.get("/test", new Handler<HttpServerRequest>() {
		    public void handle(HttpServerRequest req) {
		        req.response().end("Hello World! (vertx)");
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
