package org.example;

import org.example.model.StartBot;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Setup implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        StartBot.startTgBot();
    }
}
