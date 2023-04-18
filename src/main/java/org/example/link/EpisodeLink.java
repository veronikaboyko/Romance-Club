package org.example.link;

import org.example.model.Episode;
import org.example.model.Season;
import org.example.model.Story;

import java.io.FileNotFoundException;

public class EpisodeLink extends LinkFactory {


    public EpisodeLink(Story story, Season season, Episode episode) {
        super(story, season, episode);
    }

    /**
     * @return ссылка на страницу конкретного эпизода с информацией
     * @throws FileNotFoundException
     */
    @Override
    public String makeLink() throws FileNotFoundException {
        StringBuilder link = new StringBuilder();
        season.setSeasonNumber(season.getSeason().substring(season.getSeason().length() - 1));
        if (Integer.parseInt (season.getSeasonNumber()) == 1){
            if (Character.isDigit(episode.getEpisode().charAt(0))) {
                episode.setEpisodeNumber(String.valueOf(episode.getEpisode().charAt(0)));
                link.append(https)
                        .append(story.makeDictNames().get(story.getName()))
                        .append(htmlAct)
                        .append(season.getSeasonNumber())
                        .append(underscore)
                        .append(episode.getEpisodeNumber());
            }
            else {
                link.append(https)
                        .append(story.makeDictNames().get(story.getName()))
                        .append(htmlBonus);
            }
        }
        else{
            if (Character.isDigit(episode.getEpisode().charAt(0))) {
                episode.setEpisodeNumber(String.valueOf(episode.getEpisode().charAt(0)));
                link.append(https)
                        .append(story.makeDictNames().get(story.getName()))
                        .append(underscore)
                        .append(season.getSeasonNumber())
                        .append(htmlAct)
                        .append(underscore)
                        .append(episode.getEpisodeNumber());
            }
            else {
                link.append(https)
                        .append(story.makeDictNames().get(story.getName()))
                        .append(underscore)
                        .append(season.getSeasonNumber())
                        .append(htmlBonus);
            }
        }
        return link.toString();
    }
}
