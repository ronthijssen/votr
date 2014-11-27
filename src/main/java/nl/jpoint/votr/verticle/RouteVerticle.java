package nl.jpoint.votr.verticle;

import nl.jpoint.votr.handler.AnswerPostHandler;
import nl.jpoint.votr.handler.ClearQuestionHandler;
import nl.jpoint.votr.handler.GetQuestionHandler;
import nl.jpoint.votr.handler.SelectQuestionHandler;
import nl.jpoint.votr.handler.ShowAnswersHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

public class RouteVerticle extends Verticle {

    public static final int SERVER_PORT = 8088;
    public static final String DB_NAME = "testdb";

    public void start() {

        final Logger log = container.logger();

        RouteMatcher routeMatcher = new RouteMatcher();

        routeMatcher.get("/api/question/:talkId", new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                new GetQuestionHandler(vertx, container).handle(req);
            }
        });

        routeMatcher.post("/api/answer/:talkId/:questionId", new Handler<HttpServerRequest>() {
            public void handle(final HttpServerRequest req) {
                req.bodyHandler(new AnswerPostHandler(vertx, container, req));
            }
        });

        routeMatcher.get("/api/admin/selectquestion/:talkId/:questionId", new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                new SelectQuestionHandler(vertx, container).handle(req);
            }
        });

        routeMatcher.get("/api/admin/clearquestion/:talkId", new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                new ClearQuestionHandler(vertx, container).handle(req);
            }
        });

        routeMatcher.get("/api/admin/showanswers/:talkId", new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                new ShowAnswersHandler(vertx, container).handle(req);
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
