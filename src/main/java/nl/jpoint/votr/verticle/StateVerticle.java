package nl.jpoint.votr.verticle;

import nl.jpoint.votr.model.Question;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

import java.util.Map;
import java.util.Set;

public class StateVerticle extends Verticle {

    public static final String ACTIVE_QUESTION_DATA = "active.questions";
    public static final String UPDATE_ACTIVE_QUESTION_BUS_ADDRESS = "update.active.question";

    public void start() {
        container.logger().info("StateVerticle start");

        EventBus eb = vertx.eventBus();
        eb.registerHandler(UPDATE_ACTIVE_QUESTION_BUS_ADDRESS, new Handler<Message>() {
            public void handle(Message message) {
                container.logger().info("Received message - updating active question data.");

                final Map<String, String> questions = vertx.sharedData().getMap(ACTIVE_QUESTION_DATA);
                questions.clear();

                JsonObject body = (JsonObject) message.body();
                Set<String> talks = body.getFieldNames();
                for (String talk : talks) {
                    JsonObject activeQuestion = body.getObject(talk);
                    if (activeQuestion != null) {
                        questions.put(talk, activeQuestion.encode());
                    }
                }
            }
        });

        // Temporarily mock the questions
        initiallyMockQuestions();
    }

    private void initiallyMockQuestions() {
        JsonObject mockQuestions = new JsonObject();
        mockQuestions.putObject("devoxx", new Question(1L, "Which talk did you like the most.", "None", "Keynote", "Dart", "VertX").asJsonObject());
        mockQuestions.putObject("JPoint", new Question(2L, "Favorite coffee?", "Americano", "Espresso", "Cappucino", "Latte").asJsonObject());
        mockQuestions.putObject("poller", null);
        vertx.eventBus().send(UPDATE_ACTIVE_QUESTION_BUS_ADDRESS, mockQuestions);
    }


}

