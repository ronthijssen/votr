package nl.jpoint.votr.verticle;

import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

public class VerticleRunner extends Verticle {

    public void start() {
        container.deployWorkerVerticle(MongoVerticle.class.getName(), new JsonObject());
        container.deployVerticle(RouteVerticle.class.getName(), new JsonObject());
    }

}
