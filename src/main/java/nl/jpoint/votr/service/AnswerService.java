package nl.jpoint.votr.service;

import nl.jpoint.votr.model.Answer;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Container;

public class AnswerService {

    private Logger log;
    private Vertx vertx;

    public AnswerService(Vertx vertx, Container container) {
        this.vertx = vertx;
        this.log = container.logger();
    }

    public void saveAnswer(Answer answer) {
        // TODO: send eventbus request to mongo verticle.
    }

}
