package com.axcdev.GachaBot.Network.Discord;

import com.axcdev.GachaBot.Network.Discord.Listeners.DeveloperListener;
import com.axcdev.GachaBot.Network.Discord.Listeners.DirectMessageListener;
import com.axcdev.GachaBot.Network.Discord.Listeners.ServerMemberListener;
import com.axcdev.GachaBot.Network.Discord.Listeners.ServerModeratorListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Discord {

    private final static Logger LOGGER = LoggerFactory.getLogger(Discord.class);

    private final static DeveloperListener DEVELOPER_LISTENER = new DeveloperListener();
    private final static DirectMessageListener DIRECT_MESSAGE_LISTENER = new DirectMessageListener();
    private final static ServerMemberListener SERVER_MEMBER_LISTENER = new ServerMemberListener();
    private final static ServerModeratorListener SERVER_MODERATOR_LISTENER = new ServerModeratorListener();
    private final static ListenerAdapter[] LISTENERS = {
            DEVELOPER_LISTENER,
            DIRECT_MESSAGE_LISTENER,
            SERVER_MEMBER_LISTENER,
            SERVER_MODERATOR_LISTENER
    };

    private static JDA jda;

    // connect to Discord
    public static void connect(String token) {
        try {
            jda = JDABuilder.createDefault(token).build();
            // I'm not sure if this casting will work
            // IntelliJ Idea is complaining about it
            jda.addEventListener((Object[]) LISTENERS);
        } catch (Exception e) {
            LOGGER.error("Error connecting to Discord: " + e.getMessage());
        }
    }



    public static JDA getJda() {
        return jda;
    }
}
