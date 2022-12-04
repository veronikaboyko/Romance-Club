package org.example.telegram;
import org.example.keyboard.Automate;
import org.example.model.Episode;
import org.example.model.Story;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.toIntExact;
public class TelegramBot extends TelegramLongPollingBot {
    int count = 1;
    String info = "Последнее обновление игры - 10 ноября 2022 года" +
            "Когда следующее обновление? - 29-31 декабря 2022 года";
    private final String token;
    SendMessage sm;
    Story story = new Story();
    Episode episode = new Episode();
    String list;
    String[] splitList;
    boolean flagEpisode = true;
    boolean[] flags = Automate.Story.SetFlags();
    @Override
    public String getBotUsername() {
        return "Tutorial bot";
    }

    @Override
    public String getBotToken() {
        return token ;
    }

    @Override
    public void onUpdateReceived(Update update) {
        String answer;
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chat_id = update.getMessage().getChatId();
            if (update.getMessage().getText().equals("/start")) {
                SendMessage message = new SendMessage();// Create a message object object
                message.setChatId(chat_id);
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
            }
            else {
                sendText(chat_id, update.getMessage().getText());
            }

        }
        else if (update.hasCallbackQuery()) {
            // Set variables
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            if (call_data.equals("да")) {
                try {
                    answer = story.printTitles();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                EditMessageText new_message = new EditMessageText();
                new_message.setChatId(chat_id);
                new_message.setMessageId(toIntExact(message_id));
                new_message.setText(answer);
                try {
                    execute(new_message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public TelegramBot(String token, String botName) {
        this.token = token;
    }
    public void sendText(Long who, String what){
        try {
            if (what.equals("/info")) {
                SendMessage test = new SendMessage();
                test.setChatId(who);
                test.setText(info);
                execute(test);
            }
            else{
                if(flags[0]) {
                    Season(who,what);
                }
                else if(flags[1]) {
                    if (what.equals("/restart")){
                        Restart(who);
                    }
                    else if (what.equals("/back")) {
                        String list = story.printTitles();
                        sm = SendMessage.builder()
                                .chatId(who.toString())
                                .text(list).build();
                        flags = Automate.Story.SetFlags();
                        execute(sm);
                    }
                    else {
                            if (story.setSeason(what)) {
                                String list = story.printEpisodes();
                                sm = SendMessage.builder()
                                        .chatId(who.toString())
                                        .text(list).build();
                                flags = Automate.Episode.SetFlags();
                                execute(sm);
                            }
                            else{
                                SendMessage test = new SendMessage();
                                test.setChatId(who);
                                test.setText("Введите название из списка");
                                execute(test);
                            }
                    }
                }
                else if(flags[2]) {
                    if (what.equals("/restart")){
                        Restart(who);
                    }
                    else if (what.equals("/back")) {
                        String list = story.printSeasons();
                        sm = SendMessage.builder()
                                .chatId(who.toString())
                                .text(list).build();
                        flags = Automate.Seasonss.SetFlags();
                        execute(sm);
                    }
                    else {
                        if (episode.setEpisode(what)) {
                            flagEpisode = false;
                            list = episode.extractActions();
                            String[] splitList = list.split("\n");
                            SendMessage test = new SendMessage();
                            test.setChatId(who);
                            test.setText(splitList[count]);
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.enableMarkdown(true);
                            sendMessage.setChatId(who);
                            sendMessage.setText(splitList[count]);
                            setButtons(sendMessage);
                            flags = Automate.Text.SetFlags();
                        }
                        else{
                            SendMessage test = new SendMessage();
                            test.setChatId(who);
                            test.setText("Вы ввели не число");
                            execute(test);
                        }
                    }
                }
                else if (flags[3]){
                    if(what.equals("/restart")){
                        Restart(who);
                    }
                    else {
                        Story(who, what);
                    }
                }
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public synchronized void setButtons(SendMessage sendMessage) throws TelegramApiException {
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add(new KeyboardButton("/next"));

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add(new KeyboardButton("/before"));

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);
        execute(sendMessage);
    }
    public void Restart(Long who) throws FileNotFoundException, TelegramApiException {
        String list = story.printTitles();
        sm = SendMessage.builder()
                .chatId(who.toString())
                .text(list).build();
        execute(sm);
        flags = Automate.Story.SetFlags();
    }
    public void Season(Long who, String what) throws IOException, TelegramApiException {
            if (story.setName(what)) {
                story.seasonsAndEpisodes();
                String list = story.printSeasons();
                sm = SendMessage.builder()
                        .chatId(who.toString())
                        .text(list).build();
                flags = Automate.Seasonss.SetFlags();
                execute(sm);
            }
            else {
                SendMessage test = new SendMessage();
                test.setChatId(who);
                test.setText("Введите название из списка");
                execute(test);
            }
    }
    public void Story(Long who, String what) throws TelegramApiException, IOException {
        splitList = list.split("\n");
        SendMessage test = new SendMessage();
        if (what.equals("/next") && count < splitList.length - 1){
            test.setChatId(who);
            test.setText(splitList[count + 1]);
            count++;
            execute(test);
        }
        else if(what.equals("/before") && count > 1){
            test.setChatId(who);
            test.setText(splitList[count - 1]);
            count--;
            execute(test);
        }
        else if(count == 1){
            test.setChatId(who);
            test.setText("Начало гайда:");
            execute(test);
        }
        else if(count >= splitList.length - 1 && what.equals("/next")){
            test.setChatId(who);
            test.setText("Конец гайда:");
            execute(test);
        }
        else if(what.equals("/back")) {
            String list = story.printEpisodes();
            sm = SendMessage.builder()
                    .chatId(who.toString())
                    .text(list).build();
            flags[2] = true;
            execute(sm);
        }
    }

}

