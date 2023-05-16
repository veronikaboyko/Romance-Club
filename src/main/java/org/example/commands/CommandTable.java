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

public class CommandTable {
  TelegramBot bot;

  public CommandTable(TelegramBot bot) {
    this.bot = bot;
  }

  public void handleStartCommand(Message m) {
    long chatId = m.getChatId();
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

  public void handleInfoCommand(Message m) {
    long chatId = m.getChatId();
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

  public void handleRestartCommand(Message m) {
    try {
      long chatId = m.getChatId();
      bot.execute(bot.handler.restart(chatId));
    } catch (TelegramApiException | IOException e) {
      e.printStackTrace();
    }
  }

  public void handleBackCommand(Message m) {
    try {
      long chatId = m.getChatId();
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

  public void handleNextCommand(Message m) {
    try {
      long chatId = m.getChatId();
      String text = m.getText();
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

  public void handleBeforeCommand(Message m) {
    try {
      long chatId = m.getChatId();
      String text = m.getText();
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
