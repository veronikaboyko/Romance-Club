package org.example.telegram;
import org.example.model.Episode;
import org.example.model.Story;
import org.example.model.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class TelegramBot extends TelegramLongPollingBot {
    int count = 1;
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
        SendMessage sm;
        Story story = new Story();
        Episode episode = new Episode();
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
                    System.out.println("IOException метод start");
                }
            }
            else{
                if(flags[0]) {
                    while (true) {
                        if (story.setName(what)) {
                            break;
                        }
                    }
                    story.seasonsAndEpisodes();
                    String list = story.printSeasons();
                    sm = SendMessage.builder()
                            .chatId(who.toString())
                            .text(list).build();
                    flags[0] = false;
                    flags[1] = true;
                    execute(sm);
                }
                else if(flags[1]) {
                    if (what.equals("/restart")){
                        String list = story.printTitles();
                        sm = SendMessage.builder()
                                .chatId(who.toString())
                                .text(list).build();
                        execute(sm);
                        flags = new boolean[]{true, false, false, false};
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
                        while (true) {
                            if (story.setSeason(what)) {
                                break;
                            }
                        }
                        String list = story.printEpisodes();
                        sm = SendMessage.builder()
                                .chatId(who.toString())
                                .text(list).build();
                        flags[1] = false;
                        flags[2] = true;
                        execute(sm);
                    }
                }
                else if(flags[2]) {
                    if (what.equals("/restart")){
                        String list = story.printTitles();
                        sm = SendMessage.builder()
                                .chatId(who.toString())
                                .text(list).build();
                        execute(sm);
                        flags = new boolean[]{true, false, false, false};
                    }
                    else if (what.equals("/back")) {
                        String list = story.printSeasons();
                        sm = SendMessage.builder()
                                .chatId(who.toString())
                                .text(list).build();
                        flags[2] = false;
                        flags[1] = true;
                        execute(sm);
                    } else {
                        while (flagEpisode) {
                            if (episode.setEpisode(what)) {
                                flagEpisode = false;
                                list = episode.extractActions();
                                String[] splitList = list.split("\n");
                                SendMessage test = new SendMessage();
                                test.setChatId(who);
                                test.setText(splitList[count]);
                                execute(test);
                                break;
                            }
                        }
                    }
                    flags[2] = false;
                    flags[3] = true;
                }
                else if (flags[3]){
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
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void read(String what) throws IOException {

        Story story = new Story();
        while (true) {
            if(story.setName(what)){
                break;
            }
        }
        story.seasonsAndEpisodes();
        story.printSeasons();
        while(true) {
            if(story.setSeason(what)){
                break;
            }
        }
        story.printEpisodes();

        Episode episode = new Episode();

        while(true) {
            if(episode.setEpisode(what)){
                break;
            }
        }
        episode.extractActions();

    }

}