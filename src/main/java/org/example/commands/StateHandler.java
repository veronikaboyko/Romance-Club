package org.example.commands;

import java.io.IOException;

import org.example.jpa.StateEntity2;
import org.example.jpa.StateEntity2Repo;
import org.example.keyboard.FinalStateAutomate;
import org.example.keyboard.MakeKeyBoard;
import org.example.model.Episode;
import org.example.model.HtmlParser;
import org.example.model.Season;
import org.example.model.Story;
import org.example.telegram.HandlerForSeasons;
import org.example.telegram.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Классы для смены состояний бота.
 */
public class StateHandler {

  TelegramBot bot;
  CommandTable commandTable;
  StateEntity2Repo stateEntity2Repo;

  public StateHandler(TelegramBot bot, CommandTable commandTable,StateEntity2Repo stateEntity2Repo) {
    this.bot = bot;
    this.commandTable = commandTable;
    this.stateEntity2Repo = stateEntity2Repo;
  }

  /**
   * Метод выполенения состояния STORY.
   *
   * @param messageUser - то что ввел пользователь в боте
   */
  public void storyState(Message messageUser) {
    try {
      long chatId = messageUser.getChatId();
      String text = messageUser.getText();
      HandlerForSeasons handlerForSeasons = new HandlerForSeasons();
      bot.story = new Story();
      SendMessage smg = handlerForSeasons.getEpisodeFromSeasons(chatId, text);
      bot.execute(smg);
      if (smg.getText().equals("Введите название из списка")) {
        bot.automate = FinalStateAutomate.START;
      }
    } catch (TelegramApiException | IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Меняет выполнения  состояние SEASONSS.
   *
   * @param messageUser - то что вводить пользователь
   */
  public void seasonsState(Message messageUser) {
    try {
      long chatId = messageUser.getChatId();
      String text = messageUser.getText();
      bot.season = new Season(bot.story);
      bot.episode = new Episode(bot.story, bot.season);
      SendMessage sm = new SendMessage();
      if (text.equals("/back")) {
        commandTable.handleBackCommand(messageUser);
      } else {
        bot.season.setSeason(text);
        if (bot.season.getSeasonFlag()) {
          String list = bot.episode.printEpisodes();
          sm.setChatId(chatId);
          sm.setText((list));
          bot.execute(sm);
        } else {
          SendMessage test = new SendMessage();
          test.setChatId(chatId);
          bot.automate = FinalStateAutomate.STORY;
          test.setText(("Введите название из списка"));
          bot.execute(test);
        }
      }
    } catch (TelegramApiException | IOException ex) {
      throw new RuntimeException(ex);
    }
  }
  /**
   * Меняет выполнения  состояние EPISODE.
   *
   * @param messageUser - то что вводить пользователь
   */

  public void episodeState(Message messageUser) {
    try {
      long chatId = messageUser.getChatId();
      String text = messageUser.getText();
      if (text.equals("/back")) {
        commandTable.handleBackCommand(messageUser);
      } else {

        bot.episode.setEpisode(text);
        if (bot.episode.getEpisode() != null && bot.episode.getEpisode().matches("[-+]?\\d+")) {
          HtmlParser htmlParser = new HtmlParser();
          bot.list = htmlParser.extractActions(bot.story, bot.episode, bot.season);
          String[] splitList = bot.list.split("\n");
          SendMessage test = new SendMessage();
          test.setChatId(chatId);
          test.setText(splitList[bot.count]);
          SendMessage sendMessage = new SendMessage();
          sendMessage.enableMarkdown(true);
          sendMessage.setChatId(chatId);
          sendMessage.setText((splitList[bot.count]));
          MakeKeyBoard keyBoard = new MakeKeyBoard();
          SendMessage message2;
          message2 = keyBoard.setButtons(sendMessage);
          stateEntity2Repo.save(new StateEntity2(null,bot.story.getName(),
                  bot.season.getSeason(),bot.episode.getEpisode()));
          bot.execute(message2);
        } else {
          SendMessage test = new SendMessage();
          test.setChatId(chatId);
          bot.automate = FinalStateAutomate.SEASONSS;
          test.setText(("Вы ввели не число"));
          bot.execute(test);
        }
      }
    } catch (TelegramApiException | IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
