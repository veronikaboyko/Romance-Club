package TelegramBot;

import org.example.telegram.HandlerForSeasons;
import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.*;

public class TestForHandlerSeasons {
  @Test
  public void testGetEpisodeFromSeasons() throws IOException {
    HandlerForSeasons handler = new HandlerForSeasons();
    assertEquals("Введите название из списка", handler.getEpisodeFromSeasons(1L, "2").getText());
  }
}
