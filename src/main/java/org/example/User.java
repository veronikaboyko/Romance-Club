package org.example;

import java.io.IOException;
import java.util.Scanner;

public class User {

    public void start(){
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
                System.out.println("help");
            else{
                System.out.println("Введите команду");
            }
        }
    }
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
