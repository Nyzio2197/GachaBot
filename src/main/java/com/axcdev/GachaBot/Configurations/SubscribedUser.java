package com.axcdev.GachaBot.Configurations;

import com.axcdev.GachaBot.Network.Discord.Discord;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SubscribedUser {

    private static final List<SubscribedUser> subscribedUsers = new ArrayList<>();
    private final Logger LOGGER = LoggerFactory.getLogger(SubscribedUser.class);

    private final String name;
    private final Long userId;

    private SubscribedUser(User user) {
        this.name = user.getName();
        this.userId = user.getIdLong();
    }

    public SubscribedUser getSubscribedUser(User user) {
        for (SubscribedUser subscribedUser : subscribedUsers) {
            if (subscribedUser.getUserId().equals(user.getIdLong())) {
                return subscribedUser;
            }
        }
        SubscribedUser newSubscribedUser = new SubscribedUser(user);
        LOGGER.debug("New subscribed user: " + newSubscribedUser.getName());
        subscribedUsers.add(newSubscribedUser);
        return newSubscribedUser;
    }

    public String getName() {
        return name;
    }

    public Long getUserId() {
        return userId;
    }

    public void sendMessage(String message) {
        User user = Discord.getJda().getUserById(userId);
        if (user != null) {
            user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(message).queue());
        } else {
            LOGGER.warn("User {} not found, removing.", name);
            subscribedUsers.remove(this);
        }
    }

}
