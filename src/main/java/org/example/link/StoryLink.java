package org.example.link;

import java.io.FileNotFoundException;
import org.example.model.Story;


public class StoryLink extends LinkFactory {

  public StoryLink(Story story) {
    super(story);
  }
  /**
   * @return ссылка на страницу истории с информацией
   */

  @Override
  public String makeLink() throws FileNotFoundException {
    return https + story.makeDictNames().get(story.getName()) + html;
  }
}
