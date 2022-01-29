package com.axcdev.GachaBot.Configurations;

import com.google.gson.Gson;

public class BotData {

    private static BotData self;

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
        this.inMaintenance = inMaintenance;
    }

    public String getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public void setNextMaintenanceDate(String nextMaintenanceDate) {
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    public String getNextMaintenanceDuration() {
        return nextMaintenanceDuration;
    }

    public void setNextMaintenanceDuration(String nextMaintenanceDuration) {
        this.nextMaintenanceDuration = nextMaintenanceDuration;
    }

}
