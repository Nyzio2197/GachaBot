package com.axcdev.GachaBot.Network.Discord.Listeners;

import com.axcdev.GachaBot.Configurations.Server;
import com.axcdev.GachaBot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.HashMap;
import java.util.Objects;

public class ServerModeratorListener extends AnonymousListener {

    private final Logger LOGGER = LoggerFactory.getLogger(ServerModeratorListener.class);

    @Override
    public void messageReceived(MessageReceivedEvent event, Server server, TextChannel textChannel, String command) {
        // ensure the message is from a server moderator
        if (!server.isModerator(event.getMember())) {
            return;
        }
        if (command.length() > 0) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            switch (command) {
                case "toggles":
                    // show all toggles as an embed
                    embedBuilder.setTitle(server.getName() + " toggles")
                            .setColor(Color.CYAN); // I like cyan. Bot defaults to cyan. If you don't like it, fork and change it yourself.
                    HashMap<String, Boolean> hashMap = server.getToggleHashMap();
                    for (String key : hashMap.keySet()) {
                        embedBuilder.addField(key, hashMap.get(key) ? "ON" : "OFF", true);
                    }
                    textChannel.sendMessage((Message) embedBuilder.build()).queue();
                    break;
                case "toggle":
                    // toggle a toggle
                    String[] args = command.split(" ");
                    if (args.length > 1) {
                        String toggle = args[1];
                        if (server.getToggleHashMap().containsKey(toggle)) {
                            server.getToggleHashMap().put(toggle, !server.getToggleHashMap().get(toggle));
                            LOGGER.debug("{} in {} changed toggle {} to {}", event.getAuthor().getName(), server.getName(), toggle, server.getToggleHashMap().get(toggle));
                        } else {
                            textChannel.sendMessage("Toggle not found.").queue();
                            textChannel.sendMessage("The bot is case sensitive. Use `" + Main.getBotData().getPrefix() + "toggles` to see all toggles.").queue();
                        }
                    }
                case "channels":
                    // show all channels as an embed
                    embedBuilder.setTitle(server.getName() + " channels");
                    for (String key : server.getGroupAndChannelIds().keySet()) {
                        // change list of channel ids to list of mentionable channel names
                        StringBuilder channelNames = new StringBuilder();
                        for (Long channelId : server.getGroupAndChannelIds().get(key)) {
                            channelNames.append(Objects.requireNonNull(textChannel.getGuild().getTextChannelById(channelId)).getAsMention()).append(" ");
                        }
                        embedBuilder.addField(key, channelNames.toString(), true);
                    }
                    textChannel.sendMessage((Message) embedBuilder.build()).queue();
                    break;
                case "channel":
                    // toggle a set of channels
                    String group = command.split(" ")[1];
                    if (server.getGroupAndChannelIds().containsKey(group)) {
                        StringBuilder addedChannels = new StringBuilder();
                        StringBuilder removedChannels = new StringBuilder();
                        for (TextChannel channel : event.getMessage().getMentionedChannels()) {
                            if (server.getGroupAndChannelIds().get(group).contains(channel.getIdLong())) {
                                removedChannels.append(channel.getAsMention()).append(" ");
                                server.removeChannel(group, channel);
                            } else {
                                addedChannels.append(channel.getAsMention()).append(" ");
                                server.addChannel(group, channel);
                            }
                        }
                        textChannel.sendMessage("Channels changed").queue();
                        if (addedChannels.length() > 0) {
                            textChannel.sendMessage("Added channels: " + addedChannels).queue();
                        }
                        if (removedChannels.length() > 0) {
                            textChannel.sendMessage("Removed channels: " + removedChannels).queue();
                        }
                        LOGGER.debug("{} in {} added {} and removed {} from  {}", event.getAuthor().getName(), server.getName(), addedChannels, removedChannels, group);
                    } else {
                        textChannel.sendMessage("Channel group not found.").queue();
                        textChannel.sendMessage("Valid channel groups are: " + server.getGroupAndChannelIds().keySet()).queue();
                    }
                    break;
                case "security":
                    String[] args2 = command.split(" ");
                    if (args2.length > 1 && args2[1].matches("[0-3]")) {
                        LOGGER.debug("{} in {} changed security level to {}", event.getAuthor().getName(), server.getName(), args2[1]);
                        int security = Integer.parseInt(args2[1]);
                        server.editModerationSecurity(security);
                    } else {
                        textChannel.sendMessage("Invalid security level.").queue();
                        textChannel.sendMessage("Valid security levels are: \n" + server.getSecurityLevels()).queue();
                    }
                default:
                    textChannel.sendMessage("Command not found.").queue();
                    textChannel.sendMessage("Use `" + Main.getBotData().getPrefix() + "help` to see all commands.").queue();
                    break;
            }
        } else {
            // TODO: Implement empty ping handling
        }
    }
}
