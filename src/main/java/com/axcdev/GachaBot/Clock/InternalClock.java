package com.axcdev.GachaBot.Clock;

import com.axcdev.GachaBot.Configurations.BotData;
import com.axcdev.GachaBot.Main;

import java.text.SimpleDateFormat;
import java.util.*;

public class InternalClock {

    private static final List<ClockListener> listeners = new ArrayList<>();

    private static String currentTime;

    public static void start() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HHmmss");
                TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
                dateFormat.setTimeZone(timeZone);
                for (ClockListener clockListener : new ArrayList<>(listeners)) {
                    if (clockListener.check((currentTime = dateFormat.format(new Date(new Date().getTime() + Main.getBotData().getDaylightSavingTimeOffset()))))) {
                        clockListener.doAction();
                    }
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }

    public static void attachListener(ClockListener listener) {
        listeners.add(listener);
    }

    public static String getCurrentTime() {
        return currentTime;
    }
}
