package nl.jpoint.votr.handler;

import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Container;

public class AnswerGetHandler {

    private Logger log;

    public AnswerGetHandler(Container container) {
        this.log = container.logger();
    }

    public void handle(HttpServerRequest httpRequest) {
        JsonObject response = new JsonObject();
        response.putString("status", "ok");
        httpRequest.response().end(response.encode());
    }
}
