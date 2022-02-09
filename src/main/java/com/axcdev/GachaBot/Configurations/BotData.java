package com.axcdev.GachaBot.Configurations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class BotData {

    private final static Logger LOGGER = LoggerFactory.getLogger(BotData.class);

    private static BotData self;

    // bot prefix and status
    private String prefix;
    private String status;

    // whether to write logs to file
    private boolean logToFile;

    // store generated files locally, on DropBox, or both
    private boolean localFileStorage;
    private transient String DropBoxToken;

    // Twitter OAuth
    private transient String TwitterOAuthConsumerKey;
    private transient String TwitterOAuthConsumerSecret;
    private transient String TwitterOAuthAccessToken;
    private transient String TwitterOAuthAccessTokenSecret;
    private Set<Long> TwitterFollowingIds;

    // Discord authentication
    private transient String DiscordToken;
    private Set<Long> DeveloperDiscordIds;

    // maintenance times and duration
    private boolean inMaintenance;
    private String nextMaintenanceDate; // format MM/dd/yyyy
    private String nextMaintenanceDuration;

    // Only allow self to create
    private BotData() {}

    // Get singleton
    public static BotData getSingleton(String json) {
        if (self == null)
            self = new Gson().fromJson(json, BotData.class);
        return self;
    }

    // API
    public String getDropBoxToken() {
        return DropBoxToken;
    }

    public String getTwitterOAuthConsumerKey() {
        return TwitterOAuthConsumerKey;
    }

    public String getTwitterOAuthConsumerSecret() {
        return TwitterOAuthConsumerSecret;
    }

    public String getTwitterOAuthAccessToken() {
        return TwitterOAuthAccessToken;
    }

    public String getTwitterOAuthAccessTokenSecret() {
        return TwitterOAuthAccessTokenSecret;
    }

    public String getDiscordToken() {
        return DiscordToken;
    }

    // User interaction
    public String getPrefix() {
        return prefix;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isInMaintenance() {
        return inMaintenance;
    }

    public void setInMaintenance(boolean inMaintenance) {
        LOGGER.debug("Setting inMaintenance to " + inMaintenance);
        this.inMaintenance = inMaintenance;
    }

    public String getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public void setNextMaintenanceDate(String nextMaintenanceDate) {
        LOGGER.debug("Setting nextMaintenanceDate to " + nextMaintenanceDate);
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    public String getNextMaintenanceDuration() {
        return nextMaintenanceDuration;
    }

    public void setNextMaintenanceDuration(String nextMaintenanceDuration) {
        LOGGER.debug("Setting nextMaintenanceDuration to " + nextMaintenanceDuration);
        this.nextMaintenanceDuration = nextMaintenanceDuration;
    }

    public Set<Long> getDeveloperDiscordIds() {
        return DeveloperDiscordIds;
    }

    public String toString() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        return gson.toJson(self);
    }
}
