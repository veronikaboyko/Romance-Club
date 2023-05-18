package TelegramBot;

import org.example.model.Season;
import org.example.model.Story;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestForSeason {
  @Test
  public void testForSeason() throws IOException {
    Story story = new Story();
    story.setName("Пси");
    Season season = new Season(story);
    season.setSeason("Сезон 2");
    assertEquals("Сезон 2", season.getSeason());
    assertTrue(season.getSeasonFlag());
  }
}
