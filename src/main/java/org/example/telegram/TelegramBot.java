package org.example.telegram;

import org.example.commands.CommandTable;
import org.example.commands.StateHandler;
import org.example.keyboard.FinalStateAutomate;
import org.example.model.Episode;
import org.example.model.Season;
import org.example.model.Story;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;



public class TelegramBot extends TelegramLongPollingBot {
    public int count = 1;
    private final String token;

    public Story story;
    public Season season;
    public Episode episode;
    public String list;
    public FinalStateAutomate automate = FinalStateAutomate.Start;
    public HandlerForStory handler = new HandlerForStory();

    public TelegramBot(String token, String botName) {
        this.token = token;
    }

    @Override
    public String getBotUsername() {
        return "Tutorial bot";
    }

    @Override
    public String getBotToken() {
        return token;
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleTextMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }

    private void handleTextMessage(Message message) {
        CommandTable commandTable = new CommandTable(this);
        switch (message.getText()) {
            case "/start" -> commandTable.handleStartCommand(message);
            case "/info" -> commandTable.handleInfoCommand(message);
            default -> checkState(message);
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String callData = callbackQuery.getData();
        long messageId = callbackQuery.getMessage().getMessageId();
        long chatId = callbackQuery.getMessage().getChatId();
        switch (callData) {
            case "да":
                String answer;
                try {
                    answer = story.printTitles();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                EditMessageText newMessage = new EditMessageText();
                newMessage.setChatId(chatId);
                newMessage.setMessageId(Math.toIntExact(messageId));
                newMessage.setText(answer);
                try {
                    execute(newMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
            case "Когда следующее обновление?":
                newMessage = new EditMessageText();
                newMessage.setChatId(chatId);
                newMessage.setMessageId(Math.toIntExact(messageId));
                newMessage.setText("10-12 мая 2023 года");
                try {
                    execute(newMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void checkState(Message message) {
        automate = automate.nextState(message.getText());
        CommandTable commandTable = new CommandTable(this);
        StateHandler stateHandler = new StateHandler(this, commandTable);
        switch (automate) {
            case Start:
                commandTable.handleRestartCommand(message);
            case Restart:
                commandTable.handleRestartCommand(message);
                break;
            case Story:
                stateHandler.storyState(message);
                break;
            case Seasonss:
                stateHandler.seasonsState(message);
                break;
            case Episode:
                stateHandler.episodeState(message);
                break;
            case Text:
                switch (message.getText()) {
                    case "next" -> commandTable.handleNextCommand(message);
                    case "before" -> commandTable.handleBeforeCommand(message);
                    case "/back" -> commandTable.handleBackCommand(message);
                }
                break;
        }
    }
}