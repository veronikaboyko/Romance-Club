package org.example.control;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;

public class Info implements Commands{
    @Override
    public SendMessage doCommand(SendMessage sendMessage, Long who) throws IOException {
        SendMessage message = new SendMessage();// Create a message object object
        message.setChatId(who);
        message.setText(("Последнее обновление игры - 10 ноября 2022 года"));
        return message;
    }
}
