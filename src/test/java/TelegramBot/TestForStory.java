package TelegramBot;

import org.example.link.StoryLink;
import org.example.model.Story;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestForStory {
  @Test
  public void testForStory() throws IOException {
    Story story = new Story();
    story.setName("Пси");
    StoryLink storyLink = new StoryLink(story);
    assertEquals("Пси", story.getName());
    assertTrue(story.getNameFlag());
    assertEquals(
        "https://gamesisart.ru/guide/Romance_Club_Prohozhdenie_Psi.html", storyLink.makeLink());
  }
}
