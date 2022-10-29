package org.example;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Story implements Page {
    protected static String name;
    protected static String season;

    public void setName(String Name){
        this.name = Name;
    }

    public void setSeason(String season){
        this.season = season;
    }

    public HashMap<String, String> makeDictNames() throws FileNotFoundException {
        String path = "/Users/v/IdeaProjects/bot/data.txt";
        File file = new File(path);
        Scanner scannerF = new Scanner(file);
        HashMap<String, String> linkNames = new HashMap<>();
        while (scannerF.hasNextLine()) {
            String line = scannerF.nextLine();
            Pattern pattern = Pattern.compile ("\"(.+?)\"");
            Matcher matcher = pattern.matcher (line);
            int i = 0;
            String[] pair = new String[2];
            while (matcher.find()) {
                pair[i] = matcher.group(1);
                i++;
            }
            linkNames.put(pair[0], pair[1]);
        }
        return linkNames;
    }

    public void printTitles() throws FileNotFoundException {
        ArrayList<String> keys = new ArrayList<>(makeDictNames().keySet());
        for (String key : keys) System.out.println(key);
        System.out.println();
        System.out.println("Выбрать историю: ");
    }

    public String getPage(String link) {
        StringBuilder page = new StringBuilder();
        try {
            URL url = new URL(link);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                page.append(inputLine);
            in.close();
        }
        catch (IOException e){
            System.out.println("IOException");
        }
        return page.toString();
    }

    public Map<String, ArrayList<String>> seasonsAndEpisodes() throws IOException {
        String page = getPage(makeLink());

        Pattern pattern = Pattern.compile ("<table>.+?</table>");
        Matcher matcher = pattern.matcher (page);
        Pattern pattern1 = Pattern.compile (">[а-яА-ЯёЁ|0-9].+?<");
        Matcher matcher1;

        ArrayList<String> array = new ArrayList<>();
        if (matcher.find()) {
            matcher1 = pattern1.matcher(matcher.group());
            while (matcher1.find()){
                String str = matcher1.group().replaceAll("<", "");
                array.add(str.replaceAll(">", ""));
            }
        }
        Map<String, ArrayList<String>> map = new HashMap<>();
        ArrayList<String> episodes = new ArrayList<>();
        String element;
        while (array.size() != 0){
            element = array.remove(0);
            String substr = element.substring(0, 5);
            if (substr.equals("Сезон") && episodes.size() != 0 || array.size() == 0) {
                if (array.size() == 0)
                    episodes.add(element);
                map.put(episodes.remove(0), episodes);
                episodes = new ArrayList<>();
            }
            episodes.add(element);
        }
        return map;
    }

    public void printSeasons() throws IOException {
        ArrayList<String> keys = new ArrayList<>(seasonsAndEpisodes().keySet());
        for (String key : keys) System.out.println(key);
        System.out.println();
        System.out.println("Выбрать сезон: ");
    }

    public void printEpisodes() throws IOException {
        ArrayList<String> values = seasonsAndEpisodes().get(season);
        for (String value : values) System.out.println(value);
        System.out.println();
        System.out.println("Выбрать эпизод: ");

    }

    public String makeLink() throws FileNotFoundException {
        return "https://gamesisart.ru/guide/" + makeDictNames().get(name) + ".html";
    }
}