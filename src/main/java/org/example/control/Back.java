//package org.example.control;
//
//import org.example.model.Season;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//
//import java.io.IOException;
//
//import static org.example.model.Season.season;
//
//public class Back implements Commands{
////    Season season;
//
//    @Override
//    public SendMessage doCommand(SendMessage sendMessage, Long who) throws IOException {
//        String list = season.printSeason()
//        sendMessage.setChatId(who);
//        sendMessage.setText((list));
//        return sendMessage;
//    }
//}
