package me.baryonyx.treasurehunt.utils;

import java.util.concurrent.TimeUnit;

public class TimeConverter {
    public static long convertTimeToSeconds(String time) {
        String[] times = time.split("[:]+");
        return (((((Integer.valueOf(times[0]) * 24) + Integer.valueOf(times[1])) * 60) + Integer.valueOf(times[2])) * 60);
    }

    public static String convertMillisecondsToTime(long time) {
        int days = (int)TimeUnit.SECONDS.toDays(time);
        long hours = TimeUnit.SECONDS.toHours(time) - (days *24);
        long minutes = TimeUnit.SECONDS.toMinutes(time) - (TimeUnit.SECONDS.toHours(time)* 60);
        long seconds = TimeUnit.SECONDS.toSeconds(time) - (TimeUnit.SECONDS.toMinutes(time) *60);


        if (days > 0) {
            return String.format("%d Days %d Hours %d Minutes %d Seconds", days, hours, minutes, seconds);
        }
        else if (hours > 0) {
            return String.format("%d Hours %d Minutes %d Seconds", hours, minutes, seconds);
        }
        else if (minutes > 0) {
            return String.format("%d Minutes %d Seconds", minutes, seconds);
        }
        else {
            return String.format("%d Seconds", seconds);
        }
    }
}
