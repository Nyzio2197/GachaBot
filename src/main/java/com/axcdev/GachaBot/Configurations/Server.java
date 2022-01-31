package com.axcdev.GachaBot.Configurations;

import com.axcdev.GachaBot.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {

    private final String name;
    private final String guildId;

    private final List<String> generalChannelIds;
    private final List<String> announcementChannelIds;
    private final List<String> twitterChannelIds;

    // all the random internal toggles
    private final HashMap<String, Boolean> toggleHashMap;

    // serializing discord messages
    private final static class Message {

        private final String[] messageAndChannelId;

        public Message(String message, String channelId) {
            this.messageAndChannelId = new String[] {message, channelId};
        }

        public String[] getMessageAndChannelId() {
            return messageAndChannelId;
        }
    }

    // tracking the last sent list of Twitter message of a given userId
    // this is for the purposes of deletion in case an account double sends a tweet after deleting the first one
    // looking at you AzurLane_EN
    private final HashMap<Long, List<Message>> lastSentTwitterMessagesByUserId;

    public final static class Security {
        public static final Permission ADMINISTRATOR = Permission.ADMINISTRATOR;
        public static final Permission MANAGE_SERVER = Permission.MANAGE_SERVER;
        public static final Permission MANAGE_CHANNEL = Permission.MANAGE_CHANNEL;
        public static final Permission MANAGE_MESSAGE = Permission.MESSAGE_MANAGE;

        private Permission moderationSecurity;

        public void editModerationSecurity(Permission permission) {
            this.moderationSecurity = permission;
        }

        public boolean isModerator(Member member) {
            if (member == null)
                return false;
            // check if the member has the required permission or is a developer
            return member.hasPermission(moderationSecurity) ||
                    Main.getBotData().getDeveloperDiscordIds().contains(Long.parseLong(member.getId()));
        }
    }

    private final Security security;

    public Server(Guild guild) {
        guildId = guild.getId();
        name = guild.getName();
        generalChannelIds = new ArrayList<>();
        announcementChannelIds = new ArrayList<>();
        twitterChannelIds = new ArrayList<>();
        toggleHashMap = new HashMap<>();
        lastSentTwitterMessagesByUserId = new HashMap<>();
        security = new Security();
    }

}
