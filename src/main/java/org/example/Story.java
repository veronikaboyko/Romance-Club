package org.example.model;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Story implements Page {
    private boolean seasonFlag;
    private boolean nameFlag;

    public boolean getSeasonFlag(){
        return seasonFlag;
    }
    public boolean getNameFlag(){
        return nameFlag;
    }
    protected static String name;
    protected static String season;

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
        else {
        }
    }

    public void setSeason(String season) throws IOException {
        Map<String, ArrayList<String>> linkCheck = seasonsAndEpisodes();
        for (String key : linkCheck.keySet()){
            if (Check(key,season)){
                seasonFlag = true;
            }
        }
        if (seasonFlag) {
            Story.season = season;
        }
    }
    /**
     *
     * @return linkNames названия, соответствующие выбранной истории, будут использованы для создания ссылки
     * @throws FileNotFoundException
     */
    public HashMap<String, String> makeDictNames() throws FileNotFoundException {
        String path = "C:/Users/admin/IdeaProjects/bot/data.txt";
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
    /**
     *
     * @param link страница
     * @return содержание страницы
     */
    public String getPage(String link)  {
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
        }
        catch (IOException e){
            System.out.println("IOException");
        }
        finally {
            try{
                if (in!=null){
                    in.close();
                }
            }
            catch (IOException e){
                System.out.println("IOException");
            }

        }
        return page.toString();
    }
    /**
     * функция считывает информацию о количестве сезонов и количестве эпизодов в каждом сезоне
     * @return map ключ - сезон, значение - массив эпизодов этого сезона
     * @throws IOException
     */
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
    /**
     * функция выводит на экран количество доступных сезонов
     * @throws IOException
     */
    public String printSeasons() throws IOException {
        ArrayList<String> keys = new ArrayList<>(seasonsAndEpisodes().keySet());
        StringBuilder list = new StringBuilder();
        for (String key : keys) list.append(key).append('\n');
        return list.toString();
    }
    /**
     * функция выводит на экран количество и названия всех доступных эпизодов
     * @throws IOException
     */
    public String printEpisodes() throws IOException {
        ArrayList<String> keys = seasonsAndEpisodes().get(season);
        StringBuilder list = new StringBuilder();
        for (String key : keys) list.append(key).append('\n');
        return list.toString();

    }
    /**
     * @return ссылка на страницу с информацией
     * @throws FileNotFoundException
     */
    public String makeLink() throws FileNotFoundException {
        StringBuilder link = new StringBuilder("https://gamesisart.ru/guide/");
        link.append(makeDictNames().get(name)).append(".html");
        return link.toString();
    }
}
