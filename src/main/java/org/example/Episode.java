
package org.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Episode extends Story implements Page {

    private String episode;
    private String seasonNumber;
    private String episodeNumber;
    public void setEpisode(String episode){
        try {
            Integer.parseInt(episode);
            this.episode = episode;
        }
        catch (NumberFormatException e){
            this.episode = episode;
            System.out.println("Вы ввели не число");
        }
    }

    public String getEpisode() {
        return episode;
    }

    public String makeLink() throws FileNotFoundException {
        StringBuilder link = new StringBuilder();
        seasonNumber = (season.substring(season.length() - 1));
        if (Integer.parseInt (seasonNumber) == 1){
        //if (seasonNumber.equals("1")){
            if (Character.isDigit(episode.charAt(0))) {
                episodeNumber = String.valueOf(episode.charAt(0));
                link.append("https://gamesisart.ru/guide/")
                        .append(makeDictNames().get(name))
                        .append(".html#Act_")
                        .append(seasonNumber)
                        .append("_")
                        .append(episodeNumber);
            }
            else {
                link.append("https://gamesisart.ru/guide/")
                        .append(makeDictNames().get(name))
                        .append(".html#Bonus");
            }
        }
        else{
            if (Character.isDigit(episode.charAt(0))) {
                episodeNumber = String.valueOf(episode.charAt(0));
                link.append("https://gamesisart.ru/guide/")
                        .append(makeDictNames().get(name))
                        .append("_")
                        .append(seasonNumber)
                        .append(".html#Act_")
                        .append("_")
                        .append(episodeNumber);
            }
            else {
                link.append("https://gamesisart.ru/guide/")
                        .append(makeDictNames().get(name))
                        .append("_")
                        .append(seasonNumber)
                        .append(".html#Bonus");
            }
        }

        return link.toString();
    }

    /**
     * функция считывает информацию со страницы и выводит ее в консоль
     * @throws IOException
     */
    public void extractActions() throws IOException {
        String page = getPage(makeLink());
        StringBuilder firstTag = new StringBuilder();
        firstTag.append("<a name=\"Act_").append(seasonNumber).append("_").append(episodeNumber);
        Pattern pattern = Pattern.compile(firstTag.append(".+?<br/><br/><br/>").toString());
        Matcher matcher = pattern.matcher(page);
        Pattern pattern1 = Pattern.compile("<p>(.+?)</p>");
        Matcher matcher1;
        if (matcher.find()) {
            matcher1 = pattern1.matcher(matcher.group());
            int i = 1;
            String text;
            String[] tags = {"<font class=\"TextItem\">",
                    "</font> <font class=\"TextUp\">",
                    "</font>",
                    "&#34;",
                    "<font class=\"TextLove\">",
                    "<font class=\"TextGold\">"};
            while (matcher1.find()) {
                text = matcher1.group(i);
                if (text.charAt(0) != '<') {
                    text = text.replaceAll("&#34;", "\"");
                    System.out.println(text);
                }
                else {
                    for (String s : tags) {
                        if (text.contains(s)){
                            text = text.replaceAll(s, "");
                        }
                    }
                    System.out.println(text);
                }
            }
        }
    }
}