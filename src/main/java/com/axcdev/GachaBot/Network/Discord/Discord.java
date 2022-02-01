package com.axcdev.GachaBot.Network.Discord;

import net.dv8tion.jda.api.JDA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Discord {

    private final static Logger LOGGER = LoggerFactory.getLogger(Discord.class);

    private static JDA jda;

    public static JDA getJda() {
        return jda;
    }
}
