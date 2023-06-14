package org.example.telegram;

import java.io.IOException;
import org.example.model.Story;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * класс для работы бота с историями.
 */
public class HandlerForStory {
  /**
   * проверка на существование следущего абзаца в истории.
   *
   * @param whatUserSend - то что вводит пользователь
   * @param length - длина всей истории
   * @param count - номер абзаца в истории
   * @return условие существования
   */
  private boolean conditionToNext(String whatUserSend, int length, int count) {
    return (whatUserSend.equals("next") && count < length - 1);
  }

  /**
   * проверка на существование предыдущего абзаца в истории.
   *
   * @param whatUserSend - то что вводит пользователь
   * @param count - номер абзаца в истории
   * @return условие существования
   */
  private boolean conditionToBefore(String whatUserSend, int count) {
    return (whatUserSend.equals("before") && count > 1);
  }

  /**
   * проверка достижения начала истории.
   *
   * @param count - номер абзаца в истории
   * @return возвращает true если достигли начала
   */
  private boolean conditionToStart(int count) {
    return count == 1;
  }

  /**
   * проверка на достижение конца истории.
   *
   * @param whatUserSend - то что вводит пользователь
   * @param length - длина всей истории
   * @param count - номер абзаца в истории
   * @return условие достижения конца истории
   */
  private boolean conditionToEnd(String whatUserSend, int count, int length) {
    return count == length - 1 && whatUserSend.equals("next");
  }
  /** Метод Restart возвращает в начало работы гайда. */

  public SendMessage restart(Long user) throws IOException {
    Story story = new Story();
    SendMessage sm;
    String titles = story.printTitles();
    sm = SendMessage.builder().chatId(user.toString()).text(titles).build();
    return sm;
  }

  /**
   * Метод Story работает с конкретной историей: возможно переключение на абзац вперед/назад.
   *
   * @param user - id пользователя
   * @param whatUserSend - что ввел пользователь
   * @param listOfStoryText - сама история
   * @param count - итератор
   * @return абзац истории
   */
  public SendMessage getStories(Long user, String whatUserSend, String listOfStoryText, int count) {
    String[] splitList = listOfStoryText.split("\n");
    SendMessage test = new SendMessage();
    test.setChatId(user);
    if (conditionToNext(whatUserSend, splitList.length, count)) {
      test.setText((splitList[count + 1]));
    } else if (conditionToBefore(whatUserSend, count)) {
      test.setText((splitList[count - 1]));
    } else if (conditionToStart(count)) {
      test.setText(("Начало гайда:"));
    } else if (conditionToEnd(whatUserSend, count, splitList.length)) {
      test.setText(("Конец гайда:"));
    }
    return test;
  }
}
