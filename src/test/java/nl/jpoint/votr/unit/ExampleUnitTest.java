package nl.jpoint.votr.unit;

import nl.jpoint.votr.MainVerticle;
import org.junit.Test;

public class ExampleUnitTest {

  @Test
  public void testVerticle() {
      MainVerticle vertx = new MainVerticle();
      vertx.getContainer();
  }
}
