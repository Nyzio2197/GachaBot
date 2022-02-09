package com.axcdev.GachaBot.Network.Discord.Listeners;

import com.axcdev.GachaBot.Configurations.Server;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerModeratorListener extends AnonymousListener {

    private final Logger LOGGER = LoggerFactory.getLogger(ServerModeratorListener.class);

    @Override
    public void messageReceived(MessageReceivedEvent event, Server server, TextChannel textChannel, String command) {
        if (command.length() > 0) {
            switch (command) {
                case "toggles":

                    break;
                case "channels":

                    break;
            }
        } else {
            // TODO: Implement empty ping handling
        }
    }
}
