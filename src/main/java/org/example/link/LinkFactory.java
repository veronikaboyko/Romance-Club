package org.example.link;

import java.io.FileNotFoundException;
import org.example.model.Episode;
import org.example.model.Season;
import org.example.model.Story;

public abstract class LinkFactory {
  protected final String https = "https://gamesisart.ru/guide/";
  protected final String html = ".html";
  protected final String htmlAct = ".html#Act_";
  protected final String underscore = "_";
  protected final String htmlBonus = ".html#Bonus";
  protected Story story;
  protected Episode episode;
  protected Season season;

  LinkFactory(Story story) {
    this.story = story;
  }

  LinkFactory(Story story, Season season, Episode episode) {
    this.story = story;
    this.season = season;
    this.episode = episode;
  }

  public abstract String makeLink() throws FileNotFoundException;
}
