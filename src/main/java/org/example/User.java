package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class User {

    public void start() throws FileNotFoundException {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine().trim();
            if (input.equals("/start")) {
                try {
                    read();
                }
                catch (IOException e) {
                    System.out.println("IOException метод start");
                }
            }
            else if (input.equals("/help"))
            {
                String path = "/Users/v/IdeaProjects/bot/help.txt";
                File file = new File(path);
                Scanner help = new Scanner(file);
                while (help.hasNextLine()) {
                    String line = help.nextLine();
                    System.out.println(line);
                }
            }
            else {
                System.out.println("Введите команду");
            }
        }
    }

    /**
     * функция для чтения данных, введенных пользователем
     * @throws IOException
     */
    public void read() throws IOException {
        Scanner scanner = new Scanner(System.in);

        Story story = new Story();
        story.printTitles();
        story.setName(scanner.nextLine());

        story.seasonsAndEpisodes();
        story.printSeasons();
        story.setSeason(scanner.nextLine());
        story.printEpisodes();

        Episode episode = new Episode(scanner.nextLine());
        episode.extractActions();

    }

}
