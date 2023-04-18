package org.example.model;
import org.example.telegram.TelegramBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class StartBot {
    public static String getFromProperty(String fileName, String key) {
        Properties properties = new Properties();
        try (FileInputStream fis =
                     new FileInputStream("src/main/resources/" + fileName)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Конфигурационный файл отсутствует!", e);
        }

        return properties.getProperty(key);
    }
    public static void startTgBot() throws TelegramApiException, IOException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        System.out.println(1);
        TelegramBot bot = new TelegramBot(getFromProperty("config.properties", "token"), "Tutorial bot");
        botsApi.registerBot(bot);
    }
}
