package com.axcdev.GachaBot.Network.Discord.Listeners;

import com.axcdev.GachaBot.Configurations.Server;
import com.axcdev.GachaBot.Main;
import com.axcdev.GachaBot.Network.Discord.Discord;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class AnonymousListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message message = event.getMessage();
        // check if message starts with prefix or bot got pinged
        boolean command = false;
        if (message.getContentRaw().startsWith(event.getJDA().getSelfUser().getAsMention()) ||
                (command = message.getContentRaw().startsWith(Main.getBotData().getPrefix()))) {
            messageReceived(event,
                    Server.getServer(event.getGuild()),
                    event.getTextChannel(),
                    command ? message.getContentRaw().substring(Main.getBotData().getPrefix().length()) : message.getContentRaw());
        }
    }

    public abstract void messageReceived(MessageReceivedEvent event, Server server, TextChannel textChannel, String command);

}
