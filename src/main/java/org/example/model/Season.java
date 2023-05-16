package org.example.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class Season {
  public static String season;
  private boolean seasonFlag;
  protected String seasonNumber;
  protected Collection<ArrayList<String>> episodes;
  protected Map<String, ArrayList<String>> allSeasonsAndEpisodes;

  public Season(Story story) throws IOException {
    HtmlParser htmlParser = new HtmlParser();
    this.allSeasonsAndEpisodes = htmlParser.getEpisodesInSeasons(story);
    this.episodes = allSeasonsAndEpisodes.values();
  }

  public String getSeason() {
    return season;
  }

  public String getSeasonNumber() {
    return seasonNumber;
  }

  public void setSeasonNumber(String seasonNumber) {
    this.seasonNumber = seasonNumber;
  }

  public boolean getSeasonFlag() {
    return seasonFlag;
  }

  public void setSeason(String season) throws IOException {
    for (String key : allSeasonsAndEpisodes.keySet()) {
      if (check(key, season)) {
        seasonFlag = true;
      }
    }
    if (seasonFlag) {
      Season.season = season;
    }
  }
  /**
   * функция выводит на экран количество доступных сезонов
   *
   * @throws IOException
   */

  public String printSeasons() throws IOException {
    StringBuilder list = new StringBuilder();
    for (String key : allSeasonsAndEpisodes.keySet()) {
      list.append(key).append('\n');
    }
    return list.toString();
  }

  public boolean check(String arg1, String arg2) {
    return Objects.equals(arg1, arg2);
  }
}
