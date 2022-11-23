package org.example.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Episode extends Story implements Page {

    private String episode;
    private String seasonNumber;
    private String episodeNumber;
    public boolean setEpisode(String episode){
        try {
            Integer.parseInt(episode);
            this.episode = episode;
            return true;
        }
        catch (NumberFormatException e){
            System.out.println("Вы ввели не число");
            return false;
        }
    }

    public String makeLink() throws FileNotFoundException {
        String link;
        seasonNumber = (season.substring(season.length() - 1));
        if (seasonNumber.equals("1")){
            if (Character.isDigit(episode.charAt(0))) {
                episodeNumber = String.valueOf(episode.charAt(0));
                link = "https://gamesisart.ru/guide/" + makeDictNames().get(name)
                        + ".html#Act_" + seasonNumber + "_" + episodeNumber;
            }
            else {
                link = "https://gamesisart.ru/guide/" + makeDictNames().get(name)
                        + ".html#Bonus";
            }
        }
        else{
            if (Character.isDigit(episode.charAt(0))) {
                episodeNumber = String.valueOf(episode.charAt(0));
                link = "https://gamesisart.ru/guide/" + makeDictNames().get(name) + "_"
                        + seasonNumber + ".html#Act_" + seasonNumber + "_" + episodeNumber;
            }
            else {
                link = "https://gamesisart.ru/guide/" + makeDictNames().get(name) + "_"
                        + seasonNumber + ".html#Bonus";
            }
        }

        return link;
    }

    public String extractActions() throws IOException {
        String tempString = "";
        String page = getPage(makeLink());
        String firstTag = "<a name=\"Act_" + seasonNumber + "_" + episodeNumber;
        Pattern pattern = Pattern.compile(firstTag + ".+?<br/><br/><br/>");
        Matcher matcher = pattern.matcher(page);
        Pattern pattern1 = Pattern.compile("<p>(.+?)</p>");
        Matcher matcher1;
        if (matcher.find()) {
            matcher1 = pattern1.matcher(matcher.group());
            int i = 1;
            String string;
            String[] nonType = {"<font class=\"TextItem\">",
            "</font> <font class=\"TextUp\">",
            "</font>",
            "&#34;",
            "<font class=\"TextLove\">",
            "<font class=\"TextGold\">"};
            while (matcher1.find()) {
                string = matcher1.group(i);
                if (string.charAt(0) != '<') {
                    string = string.replaceAll("&#34;", "\"");
                    tempString += '\n' + string;
                }
                else {
                    for (String s : nonType) {
                        if (string.contains(s)){
                            string = string.replaceAll(s, "");
                        }
                    }
                    tempString += string;
                }
            }
        }
        return tempString;
    }
}
