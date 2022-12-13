package org.example.telegram;

import org.example.keyboard.Automate;
import org.example.keyboard.MakeKeyBoard;
import org.example.model.Episode;
import org.example.model.Story;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

public class TelegramBot extends TelegramLongPollingBot {
    /**
     * переменная для итерации по истории
     */
    private int count = 1;
    private final String token;
    SendMessage sm;
    Story story = new Story();
    Episode episode = new Episode();
    String list;
    /**
     * начальное состояние бота
     */
    Automate automate = Automate.Start;

    @Override
    public String getBotUsername() {
        return "Tutorial bot";
    }

    @Override
    public String getBotToken() {
        return token;
    }

    /**
     * метод работает с телеграмм ботом:
     * проверяет есть ли новое сообщение в боте
     * обрабатывает его и выводит Inline клаваиатуру при запуске
     * Выводит информацию об обновлении гайда и уточнение о запуске бота
     */
    @Override
    public void onUpdateReceived(Update update) {
        String answer;
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            if (update.getMessage().getText().equals("/start")) {
                SendMessage message = new SendMessage();// Create a message object object
                message.setChatId(chatId);
                automate = Automate.Start;
                message.setText("Вы хотите начать?");
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText("Да");
                button.setCallbackData("да");
                rowInline.add(button);
                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (update.getMessage().getText().equals("/info")) {
                SendMessage message = new SendMessage();// Create a message object object
                message.setChatId(chatId);
                message.setText("Последнее обновление игры - 10 ноября 2022 года");
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText("Когда следующее обновление?");
                button.setCallbackData("Когда следующее обновление?");
                rowInline.add(button);
                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                try {
                    execute(message); // Sending our message object to user
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                sendText(chatId, update.getMessage().getText());
            }
        } else if (update.hasCallbackQuery()) {
            // Set variables
            String callData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            switch (callData){
                case "да":
                    try {
                        answer = story.printTitles();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    EditMessageText newMessage = new EditMessageText();
                    newMessage.setChatId(chatId);
                    newMessage.setMessageId(toIntExact(messageId));
                    newMessage.setText(answer);
                    System.out.println(callData);
                    try {
                        execute(newMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                case "Когда следующее обновление?":
                    System.out.println(callData);
                    newMessage = new EditMessageText();
                    newMessage.setChatId(chatId);
                    newMessage.setMessageId(toIntExact(messageId));
                    newMessage.setText("29-31 декабря 2022 года");
                    try {
                        execute(newMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * телеграмм токен спрятный в properties
     */
    public TelegramBot(String token, String botName) {
        this.token = token;
    }

    /**
     * логика бота
     * Проверка состояния бота и в зависимости от состояния выводит необходимую информацию
     */
    public void sendText(Long who, String what) {
        try {
            automate = automate.nextState(what);
            switch (automate){
                case Restart:
                    Handler handler = new Handler();
                    execute(handler.Restart(who, what));
                    break;
                case Story:
                    handler = new Handler();
                    execute(handler.Season(who , what));
                    break;
                case Seasonss:
                    if (what.equals("/back")) {
                        String list = story.printTitles();
                        sm = SendMessage.builder()
                                .chatId(who.toString())
                                .text(list).build();
                        automate = Automate.Start;
                        execute(sm);
                    } else {
                        story.setSeason(what);
                        if (story.getSeasonFlag()) {
                            String list = story.printEpisodes();
                            sm = SendMessage.builder()
                                    .chatId(who.toString())
                                    .text(list).build();
                            execute(sm);
                        } else {
                            SendMessage test = new SendMessage();
                            test.setChatId(who);
                            automate = Automate.Story;
                            test.setText("Введите название из списка");
                            execute(test);
                        }
                    }
                    break;
                case Episode:
                    if (what.equals("/back")) {
                        String list = story.printSeasons();
                        sm = SendMessage.builder()
                                .chatId(who.toString())
                                .text(list).build();
                        automate = Automate.Story;
                        execute(sm);
                    } else {
                        episode.setEpisode(what);
                        if (episode.getEpisodeFlag()!= null && episode.getEpisodeFlag().matches("[-+]?\\d+")) {
                            list = episode.extractActions();
                            String[] splitList = list.split("\n");
                            SendMessage test = new SendMessage();
                            test.setChatId(who);
                            test.setText(splitList[count]);
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.enableMarkdown(true);
                            sendMessage.setChatId(who);
                            sendMessage.setText(splitList[count]);
                            MakeKeyBoard keyBoard = new MakeKeyBoard();
                            SendMessage message2;
                            message2 = keyBoard.setButtons(sendMessage);
                            execute(message2);
                        } else {
                            SendMessage test = new SendMessage();
                            test.setChatId(who);
                            automate = Automate.Seasonss;
                            test.setText("Вы ввели не число");
                            execute(test);
                        }
                    }
                    break;
                case Text:
                    handler = new Handler();
                    switch (what) {
                        case "next" -> {
                            SendMessage test = handler.Story(who, what, list, count);
                            SendMessage test2 = new SendMessage();
                            test2.setChatId(who);
                            test2.setText("Конец гайда:");
                            if (!test.equals(test2)) {
                                count++;
                            }
                            execute(test);
                        }
                        case "before" -> {
                            SendMessage test = handler.Story(who, what, list, count);
                            if (count > 1) {
                                count--;
                            }
                            execute(test);
                        }
                        case "/back" -> {
                            String list = story.printEpisodes();
                            sm = SendMessage.builder()
                                    .chatId(who.toString())
                                    .text(list).build();
                            automate = Automate.Seasonss;
                            execute(sm);
                        }
                    }
                    break;
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
