package org.example.telegram;

import org.example.model.Episode;
import org.example.model.HTMLParser;
import org.example.model.Season;
import org.example.model.Story;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;

/**
 * Класс для работы телеграмм бота с Сезонами
 */
public class HandlerForSeasons{
    /**
     * метод для вывода эпизодов в телеграмм
     * @param who - id
     * @param what - что ввел пользователь
     * @return - эпизоды
     */
    public SendMessage getEpisodeFromSeasons(Long who, String what) throws IOException{
        Story story = new Story();
        story.setName(what);
        if (story.getNameFlag()) {
            Season season = new Season(story);
            String list = season.printSeasons();
            SendMessage sm = new SendMessage();
            sm.setChatId(who);
            sm.setText(list);
            return sm;
        }
        else {
            SendMessage sm = new SendMessage();
            sm.setChatId(who);
            sm.setText("Введите название из списка");
            return sm;
        }
    }
}
