package org.example.telegram;
import org.example.model.Episode;
import org.example.model.Story;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TelegramBot extends TelegramLongPollingBot {
    int count = 1;
    SendMessage sm;
    Story story = new Story();
    Episode episode = new Episode();
    String list;
    boolean flagEpisode = true;
    boolean[] flags = {false,false,false,false};
    @Override
    public String getBotUsername() {
        return "Tutorial bot";
    }

    @Override
    public String getBotToken() {
        return "5985228574:AAEMIONxoELzRktGTOOJjOWN2OVBR3_x6UM";
    }

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        var id = user.getId();
        sendText(id, msg.getText());

    }
    public void sendText(Long who, String what){
        try {
            if (what.equals("/start")) {
                try {
                    String list = story.printTitles();
                    sm = SendMessage.builder()
                            .chatId(who.toString())
                            .text(list).build();
                    flags[0] = true;
                    execute(sm);
                }
                catch (IOException e) {
                    SendMessage test = new SendMessage();
                    test.setChatId(who);
                    test.setText("IOException метод start");
                    execute(test);
                }
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
                        flags[1] = false;
                        flags[0] = true;
                        execute(sm);
                    }
                    else {
                            if (story.setSeason(what)) {
                                String list = story.printEpisodes();
                                sm = SendMessage.builder()
                                        .chatId(who.toString())
                                        .text(list).build();
                                flags[1] = false;
                                flags[2] = true;
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
                        flags[2] = false;
                        flags[1] = true;
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
                                execute(test);
                                flags[2] = false;
                                flags[3] = true;
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
    public void Restart(Long who) throws FileNotFoundException, TelegramApiException {
        String list = story.printTitles();
        sm = SendMessage.builder()
                .chatId(who.toString())
                .text(list).build();
        execute(sm);
        flags = new boolean[]{true, false, false, false};
    }
    public void Season(Long who, String what) throws IOException, TelegramApiException {
            if (story.setName(what)) {
                story.seasonsAndEpisodes();
                String list = story.printSeasons();
                sm = SendMessage.builder()
                        .chatId(who.toString())
                        .text(list).build();
                flags[0] = false;
                flags[1] = true;
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
        String[] splitList = list.split("\n");
        if (what.equals("/next") && count < splitList.length - 1){
            SendMessage test = new SendMessage();
            test.setChatId(who);
            test.setText(splitList[count + 1]);
            count++;
            execute(test);
        }
        else if(what.equals("/before") && count > 1){
            SendMessage test = new SendMessage();
            test.setChatId(who);
            test.setText(splitList[count - 1]);
            count--;
            execute(test);
        }
        else if(count >= splitList.length - 1 && what.equals("/next")){
            SendMessage test = new SendMessage();
            test.setChatId(who);
            test.setText("Конец гайда:");
            execute(test);
        }
        else if(what.equals("/back")){
            String list = story.printEpisodes();
            sm = SendMessage.builder()
                    .chatId(who.toString())
                    .text(list).build();
            flags[2] = true;
            flagEpisode = true;
            execute(sm);
        }
    }

}
