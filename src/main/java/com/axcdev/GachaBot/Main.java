package com.axcdev.GachaBot;

import com.axcdev.GachaBot.Configurations.BotData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static BotData botData;

    public static void main(String[] args) {
        // ensure that a configuration json has been passed
        int expectedArgs = 2;
        if (args.length != expectedArgs) {
            LOGGER.error("Invalid number of arguments passed. Expected {}, got {}", expectedArgs, args.length);
            throw new IllegalArgumentException("Invalid number of arguments passed");
        }

        // get BotData singleton
        switch (args[0]) {
            case "file":
                try {
                    Path fileName = Path.of(args[1]);
                    String json = Files.readString(fileName);
                    LOGGER.debug("Configuration read from file: " + json);
                    botData = BotData.getSingleton(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "env":
                LOGGER.debug("Configuration read from environment variables: " + System.getenv(args[1]));
                botData = BotData.getSingleton(System.getenv(args[1]));
                break;
            default:
                LOGGER.error("Invalid configuration source: " + args[0]);
                throw new IllegalArgumentException("No BotData configuration specified");
        }
        System.out.println(botData);
        // start the bot

    }

    public static BotData getBotData() {
        return botData;
    }
}
