package TelegramBot;

import org.example.model.Episode;
import org.example.model.Season;
import org.example.model.Story;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TestEpisode {
  @Test
  public void testSetEpisode() throws IOException {
    Story story = new Story();
    story.setName("Пси");
    Season season = new Season(story);
    season.setSeason("Сезон 2");
    Episode episode = new Episode(story, season);
    episode.setEpisode("2");
    assertEquals("2", episode.getEpisode());
  }
}
