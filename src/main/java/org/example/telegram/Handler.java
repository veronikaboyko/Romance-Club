
package org.example.telegram;
import org.example.model.Episode;
import org.example.model.Story;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.io.IOException;
public class Handler {
    /**
     * проверка на существование следущего абзаца в истории
     * @param what - то что вводит пользователь
     * @param length - длина всей истории
     * @param count - номер абзаца в истории
     * @return  условие существования
     */
    private boolean conditionToNext(String what , int length, int count){
        return (what.equals("next") && count < length - 1);
    }

    /**
     * проверка на существование предыдущего абзаца в истории
     * @param what - то что вводит пользователь
     * @param count - номер абзаца в истории
     * @return условие существования
     */
    private boolean conditionToBefore(String what , int count){
        return (what.equals("before") && count > 1);
    }

    /**
     *  проверка достижения начала истории
     * @param count - номер абзаца в истории
     * @return возвращает true если достигли начала
     */
    private boolean conditionToStart(int count){
        return count == 1;
    }

    /**
     *  проверка на достижение конца истории
     * @param what - то что вводит пользователь
     * @param what - то что вводит пользователь
     * @param length - длина всей истории
     * @param count - номер абзаца в истории
     * @return условие достижения конца истории
     */
    private boolean conditionToEnd(String what , int count, int length){
        return count == length -1 && what.equals("next");
    }
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
            story.getEpisodesInSeasons(story);
            Episode episode = new Episode();
            String list = episode.printSeasons();
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

    /**
     * Метод Story работает с конкретной историей: возможно переключение на абзац вперед/назад
     * @param who - id пользователя
     * @param what - что ввел пользователь
     * @param list - сама история
     * @param count - итератор
     * @return абзац истории
     */
    public SendMessage Story (Long who, String what , String list, int count){
        String[] splitList = list.split("\n");
        SendMessage test = new SendMessage();
        test.setChatId(who);
        if (conditionToNext(what,splitList.length,count)) {
            test.setText((splitList[count + 1 ]));
        } else if (conditionToBefore(what,count)) {
            test.setText((splitList[count - 1]));
        }else if (conditionToStart(count)) {
            test.setText(("Начало гайда:"));
        } else if (conditionToEnd(what , count,splitList.length)) {
            test.setText(("Конец гайда:"));
        }
        return test;
    }
}
