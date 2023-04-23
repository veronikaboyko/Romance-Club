package org.example.control;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class Next implements Commands{
    @Override
    public SendMessage doCommand(SendMessage sendMessage, Long who) {
        SendMessage test2 = new SendMessage();
        test2.setChatId(who);
        return test2;
    }
}
