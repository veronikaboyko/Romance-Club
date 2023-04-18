
package org.example.model;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Story {
    private final String path = "C:/Users/admin/IdeaProjects/bot2/data.txt";
    private boolean nameFlag;
    protected static String name;
    protected Set<String> seasons;
    protected  Map<String, ArrayList<String>> allSeasonsAndEpisodes;
    public Story() throws IOException {
        HTMLParser htmlParser = new HTMLParser();
        this.allSeasonsAndEpisodes = htmlParser.getEpisodesInSeasons(this);
        this.seasons = allSeasonsAndEpisodes.keySet();
    }
    public boolean getNameFlag(){
        return nameFlag;
    }
    public String getName(){
        return name;
    }
    public void setName(String name) throws FileNotFoundException {
        HashMap<String, String>linkCheck = makeDictNames();
        for (String key : linkCheck.keySet()){
            if (Check(key, name)){
                nameFlag = true;
            }
        }
        if(nameFlag) {
            Story.name = name;
        }
    }

    /**
     *
     * @return linkNames названия, соответствующие выбранной истории, будут использованы для создания ссылки
     * @throws FileNotFoundException
     */
    public HashMap<String, String> makeDictNames() throws FileNotFoundException {
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
    public boolean Check(String arg1, String arg2){
        return Objects.equals(arg1, arg2);
    }
    /**
     * функция выводит на экран названия всех доступных историй
     * @throws FileNotFoundException
     */
    public String printTitles() throws FileNotFoundException {
        ArrayList<String> keys = new ArrayList<>(makeDictNames().keySet());
        StringBuilder list = new StringBuilder();
        for (String key : keys) list.append(key).append('\n');
        return list.toString();
    }
}