package org.example.link;

import java.io.FileNotFoundException;
import org.example.model.Story;

/**
 * класс для ссылок на истории.
 */

public class StoryLink extends LinkFactory {

  public StoryLink(Story story) {
    super(story);
  }
  /**
   * Метод получения ссылки на истории.
   *
   * @return ссылка на страницу истории с информацией
   */

  @Override
  public String makeLink() throws FileNotFoundException {
    return https + story.makeDictNames().get(story.getName()) + html;
  }
}
