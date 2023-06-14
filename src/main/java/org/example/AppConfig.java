package org.example;

import java.io.IOException;

import org.example.jpa.StatEntityRespository;
import org.example.jpa.StateEntity2Repo;
import org.example.jpa.UserService;
import org.example.telegram.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import static org.example.model.StartBot.getFromProperty;

@Configuration
@ComponentScan("site.ewrey.DB_Bot")
public class AppConfig {

  @Bean
  public TelegramBot registration(UserService service, StatEntityRespository respository,
                                  StateEntity2Repo stateEntity2Repo) throws IOException {
    TelegramBot bot =
        new TelegramBot(getFromProperty("config.properties", "token"), "Tutorial bot",
                service, respository,stateEntity2Repo);
    try {
      new TelegramBotsApi(DefaultBotSession.class).registerBot(bot);
      System.out.println("Бот @" + bot.getBotUsername() + " успешно запущен!!!");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return bot;
  }
}
