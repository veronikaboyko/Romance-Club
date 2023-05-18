package org.example.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.example.keyboard.FinalStateAutomate;
import org.example.telegram.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * класс для работы с командами /back,/restart,before,next,start,info.
 */
public class CommandTable {
  TelegramBot bot;

  public CommandTable(TelegramBot bot) {
    this.bot = bot;
  }

  /**
   * метод для работы с командой start.
   *
   * @param messageUser - то что ввел пользователь
   */
  public void handleStartCommand(Message messageUser) {
    long chatId = messageUser.getChatId();
    bot.automate = FinalStateAutomate.START;
    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setText("Вы хотите начать?");
    List<InlineKeyboardButton> rowInline = new ArrayList<>();
    InlineKeyboardButton button = new InlineKeyboardButton();
    button.setText("Да");
    button.setCallbackData("да");
    rowInline.add(button);
    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
    rowsInline.add(rowInline);
    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
    markupInline.setKeyboard(rowsInline);
    message.setReplyMarkup(markupInline);
    try {
      bot.execute(message);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  /**
   * Метод для работы с info.
   *
   * @param messageUser - то что ввел пользователь
   */
  public void handleInfoCommand(Message messageUser) {
    long chatId = messageUser.getChatId();
    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setText("Последнее обновление игры - 5 апреля 2023 года");
    List<InlineKeyboardButton> rowInline = new ArrayList<>();
    InlineKeyboardButton button = new InlineKeyboardButton();
    button.setText("Когда следующее обновление?");
    button.setCallbackData("Когда следующее обновление?");
    rowInline.add(button);
    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
    rowsInline.add(rowInline);
    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
    markupInline.setKeyboard(rowsInline);
    message.setReplyMarkup(markupInline);
    try {
      bot.execute(message);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  /**
   * Метод для команды restart.
   *
   * @param messageUser - то что ввел пользователь
   */
  public void handleRestartCommand(Message messageUser) {
    try {
      long chatId = messageUser.getChatId();
      bot.execute(bot.handler.restart(chatId));
    } catch (TelegramApiException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Метод для работы с командой /back.
   *
   * @param messageUser - то что ввел пользователь
   */
  public void handleBackCommand(Message messageUser) {
    try {
      long chatId = messageUser.getChatId();
      SendMessage sm = new SendMessage();
      switch (bot.automate) {
        case SEASONSS -> {
          String list = bot.story.printTitles();
          sm.setChatId(chatId);
          sm.setText((list));
          bot.automate = FinalStateAutomate.START;
          bot.execute(sm);
        }
        case EPISODE -> {
          String list = bot.season.printSeasons();
          sm.setChatId(chatId);
          sm.setText((list));
          bot.automate = FinalStateAutomate.STORY;
          bot.execute(sm);
        }
        case TEXT -> {
          String list = bot.episode.printEpisodes();
          sm.setChatId(chatId);
          sm.setText((list));
          bot.automate = FinalStateAutomate.SEASONSS;
          bot.execute(sm);
        }
        default -> {
          sm.setText("Что то пошло не так");
          bot.execute(sm);
          bot.automate = FinalStateAutomate.START;
        }
      }
    } catch (TelegramApiException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * метод для работы с командой next.
   *
   * @param messageUser - то что ввел пользователь
   */
  public void handleNextCommand(Message messageUser) {
    try {
      long chatId = messageUser.getChatId();
      String text = messageUser.getText();
      SendMessage test = bot.handler.getStories(chatId, text, bot.list, bot.count);
      SendMessage test2 = new SendMessage();
      test2.setChatId(chatId);
      test2.setText(("Конец гайда:"));
      if (!test.equals(test2)) {
        bot.count++;
      }
      bot.execute(test);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  /**
   * Метод для работы с командой before.
   *
   * @param messageUser - то что ввел пользователь
   */
  public void handleBeforeCommand(Message messageUser) {
    try {
      long chatId = messageUser.getChatId();
      String text = messageUser.getText();
      SendMessage test = bot.handler.getStories(chatId, text, bot.list, bot.count);
      if (bot.count > 1) {
        bot.count--;
      }
      bot.execute(test);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }
}
