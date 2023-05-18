package TelegramBot;

import org.example.keyboard.FinalStateAutomate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;

import static org.example.keyboard.FinalStateAutomate.*;

@RunWith(Parameterized.class)
public class ParametrizedBotTests {
  private final FinalStateAutomate v1;
  private final FinalStateAutomate v2;

  public ParametrizedBotTests(FinalStateAutomate v1, FinalStateAutomate v2) {
    this.v1 = v1;
    this.v2 = v2;
  }

  @Parameterized.Parameters
  public static Iterable<Object[]> data() {
    return Arrays.asList(
        new Object[][] {
          {START, STORY},
          {STORY, SEASONSS},
          {SEASONSS, EPISODE},
          {EPISODE, TEXT}
        });
  }

  @Test
  public void test() {
    Assert.assertSame(v2, v1.nextState("/next"));
  }

  @Test
  public void testForRestart() {
    Assert.assertSame(RESTART, STORY.nextState("/restart"));
  }
}
