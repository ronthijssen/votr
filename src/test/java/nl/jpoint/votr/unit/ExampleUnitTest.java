package nl.jpoint.votr.unit;

import nl.jpoint.votr.verticle.RouteVerticle;
import org.junit.Test;

public class ExampleUnitTest {

  @Test
  public void testVerticle() {
      RouteVerticle vertx = new RouteVerticle();
      vertx.getContainer();
  }
}
