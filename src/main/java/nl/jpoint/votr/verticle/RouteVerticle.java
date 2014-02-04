package nl.jpoint.votr.verticle;

import nl.jpoint.votr.handler.RequestGetHandler;
import nl.jpoint.votr.handler.AnswerPostHandler;
import nl.jpoint.votr.service.MongoService;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

public class RouteVerticle extends Verticle {

    public static final int SERVER_PORT = 8080;
    public static final String DB_NAME = "testdb";

    public void start() {

        final Logger log = container.logger();

        RouteMatcher routeMatcher = new RouteMatcher();

        routeMatcher.get("/api/question/:talkId", new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                new RequestGetHandler(container).handle(req, req.params().get("talkId"));
            }
        });

        routeMatcher.post("/api/answer/1", new Handler<HttpServerRequest>() {
            public void handle(final HttpServerRequest req) {
                req.bodyHandler(new AnswerPostHandler(container, req));
            }
        });

        routeMatcher.get("/api/mongotest", new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                new MongoService(vertx, container).doTest(req);
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
