package org.example.model;

import java.io.FileNotFoundException;

public class LinkFactory {
    private final String https = "https://gamesisart.ru/guide/";
    private final String html = ".html";
    private final String htmlAct = ".html#Act_";
    private final String underscore = "_";
    private final String htmlBonus = ".html#Bonus";

    /**
     * @return ссылка на страницу истории с информацией
     * @throws FileNotFoundException
     */
    public final String makeStoryLink(Story story) throws FileNotFoundException {
        StringBuilder link = new StringBuilder(https);
        link.append(story.makeDictNames().get(story.getName())).append(html);
        return link.toString();
    }

    /**
     * @return ссылка на страницу конкретного эпизода с информацией
     * @throws FileNotFoundException
     */
    public final String makeEpisodeLink(Episode episode) throws FileNotFoundException {
        StringBuilder link = new StringBuilder();
        episode.setSeasonNumber(episode.getSeason().substring(episode.getSeason().length() - 1));
        if (Integer.parseInt (episode.getSeasonNumber()) == 1){
            if (Character.isDigit(episode.getEpisode().charAt(0))) {
                episode.setEpisodeNumber(String.valueOf(episode.getEpisode().charAt(0)));
                link.append(https)
                        .append(episode.makeDictNames().get(episode.getName()))
                        .append(htmlAct)
                        .append(episode.getSeasonNumber())
                        .append(underscore)
                        .append(episode.getEpisodeNumber());
            }
            else {
                link.append(https)
                        .append(episode.makeDictNames().get(episode.getName()))
                        .append(htmlBonus);
            }
        }
        else{
            if (Character.isDigit(episode.getEpisode().charAt(0))) {
                episode.setEpisodeNumber(String.valueOf(episode.getEpisode().charAt(0)));
                link.append(https)
                        .append(episode.makeDictNames().get(episode.getName()))
                        .append(underscore)
                        .append(episode.getSeasonNumber())
                        .append(htmlAct)
                        .append(underscore)
                        .append(episode.getEpisodeNumber());
            }
            else {
                link.append(https)
                        .append(episode.makeDictNames().get(episode.getName()))
                        .append(underscore)
                        .append(episode.getSeasonNumber())
                        .append(htmlBonus);
            }
        }
        return link.toString();
    }
}
