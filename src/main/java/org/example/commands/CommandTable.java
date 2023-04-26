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
        bot.automate = FinalStateAutomate.Start;
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Вы хотите начать?");
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Да");
        button.setCallbackData("да");
        rowInline.add(button);
        rowsInline.add(rowInline);
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
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Когда следующее обновление?");
        button.setCallbackData("Когда следующее обновление?");
        rowInline.add(button);
        rowsInline.add(rowInline);
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
                case Seasonss -> {
                    String list = bot.story.printTitles();
                    sm.setChatId(chatId);
                    sm.setText((list));
                    bot.automate = FinalStateAutomate.Start;
                    bot.execute(sm);
                }
                case Episode -> {
                    String list = bot.season.printSeasons();
                    sm.setChatId(chatId);
                    sm.setText((list));
                    bot.automate = FinalStateAutomate.Story;
                    bot.execute(sm);

                }
                case Text -> {
                    String list = bot.episode.printEpisodes();
                    sm.setChatId(chatId);
                    sm.setText((list));
                    bot.automate = FinalStateAutomate.Seasonss;
                    bot.execute(sm);

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
