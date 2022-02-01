package com.axcdev.GachaBot.Configurations;

import com.axcdev.GachaBot.Main;
import com.axcdev.GachaBot.Network.Discord.Discord;
import com.google.gson.Gson;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Server {

    private static final List<Server> servers = new ArrayList<>();
    private final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private final String name;
    private final Long guildId;

    // three valid groups to add to: General, Announcement, Twitter
    private final static String[] validGroups = {"General", "Announcement", "Twitter"};
    private final HashMap<String, List<Long>> groupAndChannelIds;

    // all the random internal toggles
    private final HashMap<String, Boolean> toggleHashMap;

    // serializing discord messages
    private final static class Message {

        private final String[] messageAndChannelId;

        public Message(String message, String channelId) {
            this.messageAndChannelId = new String[] {message, channelId};
        }

    }

    // tracking the last sent list of Twitter message of a given userId
    // this is for the purposes of deletion in case an account double sends a tweet after deleting the first one
    // looking at you AzurLane_EN
    private final HashMap<Long, List<Message>> lastSentTwitterMessagesByUserId;

    private class Security {

        // varying levels of what is considered a server moderator
        // Level 0: Administrator Permissions
        // Level 1: Server Management Permissions
        // Level 2: Channel Management Permissions
        // Level 3: Message Management Permissions
        private int moderationSecurity;
        private final Set<Long> adminUserIds;

        private Security() {
            adminUserIds = new HashSet<>();
        }

        private void editModerationSecurity(int securityLevel) {
            LOGGER.debug("Setting {} server moderation security to {}", name, securityLevel);
            moderationSecurity = securityLevel;
        }

        private boolean addModerator(Member member) {
            LOGGER.debug("Adding {} as a moderator to {}", member.getUser().getName(), name);
            return adminUserIds.add(member.getIdLong());
        }

        private boolean removeModerator(Member member) {
            LOGGER.debug("Removing {} as a moderator from {}", member.getUser().getName(), name);
            return adminUserIds.remove(member.getIdLong());
        }

        private boolean isModerator(Member member) {
            if (member == null)
                return false;
            // check if the member has the required permission or is a developer
            // please ignore this stupidly long in-line if statement
            // I couldn't think of a better way to do this
            Permission requiredPermission = moderationSecurity == 0 ? Permission.ADMINISTRATOR : moderationSecurity == 1 ? Permission.MANAGE_SERVER : moderationSecurity == 2 ? Permission.MANAGE_CHANNEL : Permission.MESSAGE_MANAGE;
            return member.hasPermission(requiredPermission) ||
                    adminUserIds.contains(member.getIdLong()) ||
                    Main.getBotData().getDeveloperDiscordIds().contains(member.getIdLong());
        }

    }

    private final Security security;

    private Server(Guild guild) {
        guildId = guild.getIdLong();
        name = guild.getName();
        groupAndChannelIds = new HashMap<>();
        // add all valid groups
        for (String group : validGroups) {
            groupAndChannelIds.put(group, new ArrayList<>());
        }
        toggleHashMap = new HashMap<>();
        lastSentTwitterMessagesByUserId = new HashMap<>();
        security = new Security();
    }

    // get an existing server or create a new one
    public Server getServer(Guild guild) {
        for (Server server : servers) {
            if (server.getGuildId().equals(guild.getIdLong()))
                return server;
        }
        Server server = new Server(guild);
        LOGGER.debug("Created new server {}", server.getName());
        servers.add(server);
        return server;
    }

    public String getName() {
        return name;
    }

    public Long getGuildId() {
        return guildId;
    }

    // wrapper for security methods
    public void editModerationSecurity(int securityLevel) {
        security.editModerationSecurity(securityLevel);
    }

    public boolean addModerator(Member member) {
        return security.addModerator(member);
    }

    public boolean removeModerator(Member member) {
        return security.removeModerator(member);
    }

    public boolean isModerator(Member member) {
        return security.isModerator(member);
    }

    // add and remove channels from the server
    public boolean addChannel(String group, TextChannel channel) {
        if (groupAndChannelIds.containsKey(group)) {
            if (!groupAndChannelIds.get(group).contains(channel.getIdLong())) {
                groupAndChannelIds.get(group).add(channel.getIdLong());
                return true;
            }
        }
        return false;
    }

    public boolean removeChannel(String group, TextChannel channel) {
        if (groupAndChannelIds.containsKey(group)) {
            if (groupAndChannelIds.get(group).contains(channel.getIdLong())) {
                groupAndChannelIds.get(group).remove(channel.getIdLong());
                return true;
            }
        }
        return false;
    }

    // push Twitter updates to Twitter channels
    public void pushTwitterUpdate(String TwitterUsername, long TwitterStatusId, long TwitterUserId) {
        // clear previous stored messages and update with new ones
        lastSentTwitterMessagesByUserId.get(TwitterUserId).clear();
        lastSentTwitterMessagesByUserId.get(TwitterUserId).add(new Message("" + TwitterStatusId, null));
        for (Long channelId : groupAndChannelIds.get(validGroups[2])) {
            TextChannel channel = Discord.getJda().getTextChannelById(channelId);
            if (channel != null) {
                try {
                    String messageString = Files.readString(Path.of("TwitterMessageFormat.config"));
                    messageString = messageString.replace("{TWITTER_STATUS}", "https://twitter.com/" + TwitterUsername + "/status/" + TwitterStatusId);
                    channel.sendMessage(messageString).queue(message -> {
                        lastSentTwitterMessagesByUserId.get(TwitterUserId).add(new Message(message.getId(), message.getChannel().getId()));
                    });
                } catch (IOException e) {
                    LOGGER.error("Could not read TwitterMessageFormat.config");
                    e.printStackTrace();
                }
            } else {
                LOGGER.warn("Could not send Twitter update to channel {} in {} server. Removing.", channelId, name);
                groupAndChannelIds.get(validGroups[2]).remove(channelId);
            }
        }
    }

    // pull Twitter updates from Twitter channels
    public void pullTwitterUpdate(long TwitterUserId, long TwitterStatusId) {
        // check if deleted message is the latest one otherwise ignore
        if (lastSentTwitterMessagesByUserId.get(TwitterUserId).get(0).messageAndChannelId[0].equals("" + TwitterStatusId)) {
            for (Message message : lastSentTwitterMessagesByUserId.get(TwitterUserId)) {
                TextChannel channel = Discord.getJda().getTextChannelById(message.messageAndChannelId[1]);
                if (channel != null) {
                    channel.deleteMessageById(message.messageAndChannelId[0]).queue();
                } else {
                    LOGGER.warn("Could not delete Twitter update from channel {} in {} server. Removing.", message.messageAndChannelId[1], name);
                    lastSentTwitterMessagesByUserId.get(TwitterUserId).remove(message);
                }
            }
        }
    }

    // utility methods
    // sending message to channel group in server
    public void sendMessage(String group, String message) {
        for (Long channelId : groupAndChannelIds.get(group)) {
            TextChannel channel = Discord.getJda().getTextChannelById(channelId);
            if (channel != null) {
                channel.sendMessage(message).queue();
            } else {
                LOGGER.warn("Could not send message to channel {} in {} server. Removing.", channelId, name);
                groupAndChannelIds.get(group).remove(channelId);
            }
        }
    }

    // serializing self into json
    public String toJson() {
        return new Gson().toJson(this);
    }



}
