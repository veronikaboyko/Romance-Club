
package org.example.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Episode extends Season {

    private String episode;
    private String episodeNumber;

    public String getEpisodeNumber(){
        return episodeNumber;
    }
    public void setEpisodeNumber(String episodeNumber){
        this.episodeNumber = episodeNumber;
    }
    public void setEpisode(String episode){
        try {
            Integer.parseInt(episode);
            this.episode = episode;
        }
        catch (NumberFormatException e){
            System.out.println("введите число");
        }
    }
    public String getEpisode(){
        return episode;
    }
    /**
     * функция выводит на экран количество и названия всех доступных эпизодов
     * @throws IOException
     */
    public String printEpisodes() throws IOException {
        HTMLParser htmlParser = new HTMLParser();
        ArrayList<String> keys = htmlParser.getEpisodesInSeasons(this).get(season);
        StringBuilder list = new StringBuilder();
        for (String key : keys) list.append(key).append('\n');
        return list.toString();
    }
}
