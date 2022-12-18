package org.example;

import org.example.model.StartBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {
    public static void main(String[] args) throws TelegramApiException {
        StartBot.startTgBot();
    }
}
