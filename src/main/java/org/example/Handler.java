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
    public SendMessage Restart(Long who, String what) throws FileNotFoundException{
        Story story = new Story();
        SendMessage sm;
        String list = story.printTitles();
        sm = SendMessage.builder()
                .chatId(who.toString())
                .text(list).build();
        return sm;
    }

    /**
     * Метод Season работает с классом Season
     * @param what - то что ввел пользователь
     */
    public SendMessage Season(Long who, String what) throws IOException, TelegramApiException {
        Story story = new Story();
        story.setName(what);
        if (story.getNameFlag()) {
            story.seasonsAndEpisodes();
            String list = story.printSeasons();
            SendMessage sm;
            sm = SendMessage.builder()
                    .chatId(who.toString())
                    .text(list).build();
            return sm;
        }
        else {
            SendMessage sm;
            sm = SendMessage.builder()
                    .chatId(who.toString())
                    .text( "Введите название из списка").build();
            return sm;
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
        if (what.equals("next") && count < splitList.length - 1) {
            test.setChatId(who);
            test.setText(splitList[count + 1]);
            return test;
        } else if (what.equals("before") && count > 1) {
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
