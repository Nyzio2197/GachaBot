package com.axcdev.GachaBot.Configurations;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotData {

    private static BotData self;
    private final static Logger logger = LoggerFactory.getLogger(BotData.class);

    // bot prefix and status
    private String prefix;
    private String status;

    // whether to write logs to file
    private boolean logToFile;

    // store generated files locally, on DropBox, or both
    private boolean localFileStorage;
    private String DropBoxToken;

    // Twitter OAuth
    private String TwitterOAuthConsumerKey;
    private String TwitterOAuthConsumerSecret;
    private String TwitterOAuthAccessToken;
    private String TwitterOAuthAccessTokenSecret;

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

    public boolean isInMaintenance() {
        return inMaintenance;
    }

    public void setInMaintenance(boolean inMaintenance) {
        logger.debug("Setting inMaintenance to " + inMaintenance);
        this.inMaintenance = inMaintenance;
    }

    public String getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public void setNextMaintenanceDate(String nextMaintenanceDate) {
        logger.debug("Setting nextMaintenanceDate to " + nextMaintenanceDate);
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    public String getNextMaintenanceDuration() {
        return nextMaintenanceDuration;
    }

    public void setNextMaintenanceDuration(String nextMaintenanceDuration) {
        logger.debug("Setting nextMaintenanceDuration to " + nextMaintenanceDuration);
        this.nextMaintenanceDuration = nextMaintenanceDuration;
    }

}
