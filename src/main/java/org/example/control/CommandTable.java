package org.example.control;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;
import java.util.HashMap;

public class CommandTable {
//    Back back = new Back();
    Next next = new Next();
    Info info = new Info();
    private final HashMap<String, SendMessage> commands = new HashMap<>();

    public CommandTable(Long who,SendMessage sendMessage) throws IOException {
//        commands.put("/back",back.doCommand(sendMessage,who));
        commands.put("next",next.doCommand(sendMessage,who));
        commands.put("/info",info.doCommand(sendMessage,who));
    }
    public HashMap<String, SendMessage> getCommandTable(){
        return commands;
    }

}
