package org.example.model;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.example.link.EpisodeLink;
import org.example.link.LinkFactory;
import org.example.link.StoryLink;

/**
 * Класс для парса ссылок
 */
public class HtmlParser {

  private final String[] replacementTags = {
    "<font class=\"TextItem\">",
    "</font> <font class=\"TextUp\">",
    "</font>",
    "&#34;",
    "<font class=\"TextLove\">",
    "<font class=\"TextGold\">"
  };

  /**
   * метод получения страницы на сайте.
   *
   * @param link ссылка на страницу
   * @return содержание страницы
   */
  public String getPage(String link) {
    StringBuilder page = new StringBuilder();
    BufferedReader in = null;
    try {
      URL url = new URL(link);
      in = new BufferedReader(new InputStreamReader(url.openStream(), "cp1251"));
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        page.append(inputLine);
      }
      in.close();
    } catch (IOException e) {
      System.out.println("IOException");
    } finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (IOException e) {
        System.out.println("IOException");
      }
    }
    return page.toString();
  }

  /**
   * функция считывает со страницы информацию о количестве сезонов и количестве эпизодов в каждом
   * сезоне.
   *
   * @return map ключ - сезон, значение - массив эпизодов этого сезона
   * @throws IOException
   */
  public Map<String, ArrayList<String>> getEpisodesInSeasons(Story story) throws IOException {
    LinkFactory linkObj = new StoryLink(story);
    String link = linkObj.makeLink();
    String page = getPage(link);
    Pattern pattern = Pattern.compile("<table>.+?</table>");
    Matcher matcher = pattern.matcher(page);
    Pattern pattern1 = Pattern.compile(">[а-яА-ЯёЁ|0-9].+?<");
    Matcher matcher1;

    ArrayList<String> array = new ArrayList<>();
    if (matcher.find()) {
      matcher1 = pattern1.matcher(matcher.group());
      while (matcher1.find()) {
        String str = matcher1.group().replaceAll("<", "");
        array.add(str.replaceAll(">", ""));
      }
    }
    Map<String, ArrayList<String>> map = new HashMap<>();
    ArrayList<String> episodes = new ArrayList<>();
    String element;
    while (array.size() != 0) {
      element = array.remove(0);
      String seasonSubstr = element.substring(0, 5);
      if (seasonSubstr.equals("Сезон") && episodes.size() != 0 || array.size() == 0) {
        if (array.size() == 0) {
          episodes.add(element);
        }
        map.put(episodes.remove(0), episodes);
        episodes = new ArrayList<>();
      }
      episodes.add(element);
    }
    return map;
  }
  /**
   * функция считывает информацию со страницы конкретного эпизода и выводит ее в консоль.
   *
   * @throws IOException
   */

  public String extractActions(Story story, Episode episode, Season season) throws IOException {
    StringBuilder tmpString = new StringBuilder();
    season.setSeasonNumber(season.getSeason().substring(season.getSeason().length() - 1));
    LinkFactory linkObj = new EpisodeLink(story, season, episode);
    String link = linkObj.makeLink();
    String page = getPage(link);
    StringBuilder startOfInformationBlock = new StringBuilder();
    startOfInformationBlock
        .append("<a name=\"Act_")
        .append(season.getSeasonNumber())
        .append("_")
        .append(episode.getEpisodeNumber());
    Pattern pattern =
        Pattern.compile(startOfInformationBlock.append(".+?<br/><br/><br/>").toString());
    Matcher matcher = pattern.matcher(page);
    Pattern pattern1 = Pattern.compile("<p>(.+?)</p>");
    Matcher matcher1;
    if (matcher.find()) {
      matcher1 = pattern1.matcher(matcher.group());
      int i = 1;
      String outputText;
      while (matcher1.find()) {
        outputText = matcher1.group(i);
        if (outputText.charAt(0) != '<') {
          outputText = outputText.replaceAll("&#34;", "\"");
          tmpString.append('\n').append(outputText);
        } else {
          for (String tag : replacementTags) {
            if (outputText.contains(tag)) {
              outputText = outputText.replaceAll(tag, "");
            }
          }
          tmpString.append(outputText);
        }
      }
    }
    return tmpString.toString();
  }
}
