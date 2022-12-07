package org.example.telegram;

import org.example.model.Story;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.io.IOException;
public class Handler {
    /**
     * Метод Restart возвращает в начало работы гайда
     */
    public String Restart() throws FileNotFoundException{
        Story story = new Story();
        return story.printTitles();
    }

    /**
     * Метод Season работает с классом Season
     * @param what - то что ввел пользователь
     */
    public String Season(String what) throws IOException, TelegramApiException {
        Story story = new Story();
        if (story.setName(what)) {
            story.seasonsAndEpisodes();
            return story.printSeasons();
        }
        else {
            return "Введите название из списка";
        }
    }

    /**
     * Метод Story работает с конкретной историей: возможно переключение на абзац вперед/назад
     * @param who - id пользователя
     * @param what - что ввел пользователь
     * @param list - сама история
     * @param count - итератор
     * @return абзац истории
     */
    public SendMessage Story (Long who, String what , String list, int count){
        System.out.println(count);
        String[] splitList = list.split("\n");
        SendMessage test = new SendMessage();
        if (what.equals("/next") && count < splitList.length - 1) {
            test.setChatId(who);
            test.setText(splitList[count + 1]);
            return test;
        } else if (what.equals("/before") && count > 1) {
            test.setChatId(who);
            test.setText(splitList[count - 1]);
            return test;
        }else if (count == 1) {
            test.setChatId(who);
            test.setText("Начало гайда:");
            return test;
        } else if (count >= splitList.length - 1 && what.equals("/next")) {
            test.setChatId(who);
            test.setText("Конец гайда:");
            return test;
        }
        return test;
    }
}
