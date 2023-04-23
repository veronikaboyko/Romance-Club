package org.example;

import org.example.telegram.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;

import static org.example.model.StartBot.getFromProperty;

@Configuration
@ComponentScan("site.ewrey.DB_Bot")
public class AppConfig {

    @Bean
    public TelegramBot registration() throws IOException {
        TelegramBot bot = new TelegramBot(getFromProperty("config.properties", "token"), "Tutorial bot");
        try {
            new TelegramBotsApi(DefaultBotSession.class).registerBot(bot);
            System.out.println("Бот @"+bot.getBotUsername()+" успешно запущен!!!");
        } catch (Exception e){
            e.printStackTrace();
        }
        return bot;
    }
}
