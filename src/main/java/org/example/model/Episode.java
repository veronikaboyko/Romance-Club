package org.example.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class Episode {

  private String episode;
  private String episodeNumber;
  protected Collection<ArrayList<String>> allEpisodes;

  public Episode(Story story, Season season) throws IOException {
    HtmlParser htmlParser = new HtmlParser();
    this.allEpisodes = htmlParser.getEpisodesInSeasons(story).values();
    System.out.println(allEpisodes);
  }

  public String getEpisodeNumber() {
    return episodeNumber;
  }

  public void setEpisodeNumber(String episodeNumber) {
    this.episodeNumber = episodeNumber;
  }

  public void setEpisode(String episode) {
    try {
      Integer.parseInt(episode);
      this.episode = episode;
    } catch (NumberFormatException e) {
      System.out.println("введите число");
    }
  }

  public String getEpisode() {
    return episode;
  }
  /**
   * функция выводит на экран количество и названия всех доступных эпизодов
   *
   * @throws IOException
   */

  public String printEpisodes() throws IOException {
    StringBuilder list = new StringBuilder();
    for (String key : allEpisodes.iterator().next()) {
      list.append(key).append('\n');
    }
    return list.toString();
  }
}
