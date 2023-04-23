package org.example.model;

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
}
