package org.example.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Season extends Story {
    protected static String season;
    private boolean seasonFlag;
    protected String seasonNumber;
    public String getSeason() {
        return season;
    }
    public String getSeasonNumber(){
        return seasonNumber;
    }
    public void setSeasonNumber(String seasonNumber){
        this.seasonNumber = seasonNumber;
    }
    public boolean getSeasonFlag(){
        return seasonFlag;
    }
    public void setSeason(String season) throws IOException {
        HTMLParser htmlParser = new HTMLParser();
        Map<String, ArrayList<String>> linkCheck = htmlParser.getEpisodesInSeasons(this);
        for (String key : linkCheck.keySet()){
            if (Check(key,season)){
                seasonFlag = true;
            }
        }
        if (seasonFlag) {
            Season.season = season;
        }
    }
    /**
     * функция выводит на экран количество доступных сезонов
     * @throws IOException
     */
    public String printSeasons() throws IOException {
        HTMLParser htmlParser = new HTMLParser();
        ArrayList<String> keys = new ArrayList<>(htmlParser.getEpisodesInSeasons(this).keySet());
        StringBuilder list = new StringBuilder();
        for (String key : keys) list.append(key).append('\n');
        return list.toString();
    }
}

