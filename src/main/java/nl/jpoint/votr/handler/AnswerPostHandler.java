package nl.jpoint.votr.handler;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Container;

public class AnswerPostHandler implements Handler<Buffer> {

    private Logger log;
    private HttpServerRequest httpRequest;

    public AnswerPostHandler(Container container, HttpServerRequest request) {
        this.log = container.logger();
        this.httpRequest = request;
    }

    @Override
    public void handle(Buffer event) {
        JsonObject request = new JsonObject(event.toString());
        log.info("request:" + request);

        // genereer response; kan ook via
        // messagebus of callback.
        JsonObject response = new JsonObject();
        response.putString("status", "ok");
        response.putObject("request", request);
        httpRequest.response().end(response.encode());
    }
}
