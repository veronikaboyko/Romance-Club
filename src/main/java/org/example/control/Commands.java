package org.example.control;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;

public interface Commands {
    SendMessage doCommand(SendMessage sendMessage,Long who) throws IOException;
}
