package com.axcdev.GachaBot;

import com.axcdev.GachaBot.Configurations.BotData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    private static BotData botData;

    public static void main(String[] args) {
        // ensure that a configuration json has been passed
        assert args.length == 2;

        // get BotData singleton
        switch (args[0]) {
            case "file":
                try {
                    Path fileName = Path.of(args[1]);
                    String json = Files.readString(fileName);
                    botData = BotData.getSingleton(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "env":
                botData = BotData.getSingleton(System.getenv(args[1]));
                break;
            default:
                throw new IllegalArgumentException("");
        }
    }

}
