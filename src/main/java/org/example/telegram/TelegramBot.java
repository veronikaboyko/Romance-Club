package org.example.telegram;

import org.example.JPA.UserEntity;
import org.example.JPA.UserService;
import org.example.commands.CommandTable;
import org.example.commands.StateHandler;
import org.example.keyboard.FinalStateAutomate;
import org.example.keyboard.MakeKeyBoard;
import org.example.model.Episode;
import org.example.model.HTMLParser;
import org.example.model.Season;
import org.example.model.Story;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TelegramBot extends TelegramLongPollingBot {
    /**
     * переменная для итерации по истории
     */
    public int count = 1;
    private final String token;
    SendMessage sm = new SendMessage();
    public Story story = new Story();
    public Season season;
    public Episode episode;
    public String list;
    /**
     * начальное состояние бота
     */
    public FinalStateAutomate automate = FinalStateAutomate.START;
    public HandlerForStory handler = new HandlerForStory();


    @Override
    public String getBotUsername() {
        return "Tutorial bot";
    }

    UserService service;

    @Override
    public String getBotToken() {
        return token;
    }

    private void handleTextMessage(Message message) {
        CommandTable commandTable = new CommandTable(this);
        switch (message.getText()) {
            case "/start" -> commandTable.handleStartCommand(message);
            case "/info" -> commandTable.handleInfoCommand(message);
            default -> checkState(message);
        }
    }

    private void checkState(Message message) {
        automate = automate.nextState(message.getText());
        CommandTable commandTable = new CommandTable(this);
        StateHandler stateHandler = new StateHandler(this, commandTable);
        switch (automate) {
            case START:
                commandTable.handleRestartCommand(message);
            case RESTART:
                commandTable.handleRestartCommand(message);
                break;
            case STORY:
                stateHandler.storyState(message);
                break;
            case SEASONSS:
                stateHandler.seasonsState(message);
                break;
            case EPISODE:
                stateHandler.episodeState(message);
                break;
            case TEXT:
                switch (message.getText()) {
                    case "next" -> commandTable.handleNextCommand(message);
                    case "before" -> commandTable.handleBeforeCommand(message);
                    case "/back" -> commandTable.handleBackCommand(message);
                }
                break;
        }
    }

    /**
     * метод работает с телеграмм ботом:
     * проверяет есть ли новое сообщение в боте
     * обрабатывает его и выводит Inline клаваиатуру при запуске
     * Выводит информацию об обновлении гайда и уточнение о запуске бота
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleTextMessage(update.getMessage());
            long chatId = update.getMessage().getChatId();
            try {

                if (update.getMessage().getText().equals("/add")) {
                    if (!service.existsByChatId(chatId)) {
                        UserEntity entity = new UserEntity();
                        entity.setChatId(chatId);
                        entity.setSubscribe(false);
                        service.save(entity);
                        sendMessage(update, "That's OK!");
                    } else sendMessage(update, "U are in table yet!");
                }
                if (update.getMessage().getText().equals("/check")) {
                    boolean flag = service.check(chatId);
                    if (flag) sendMessage(update, "You are the admin");
                    else sendMessage(update, "You are not admin");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
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

    /**
     * телеграмм токен спрятный в properties
     */
    public TelegramBot(String token, String botName, UserService service) throws IOException {
        this.token = token;
        this.service = service;
    }

    /**
     * логика бота
     * Проверка состояния бота и в зависимости от состояния выводит необходимую информацию
     */
    public void sendMessage(Update update, String text) {
        try {
            execute(
                    SendMessage.builder()
                            .chatId((update.hasMessage()) ? update.getMessage().getChatId() : update.getCallbackQuery().getFrom().getId())
                            .text(text)
                            .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
