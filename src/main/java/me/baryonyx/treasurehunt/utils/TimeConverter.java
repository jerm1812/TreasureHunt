package me.baryonyx.treasurehunt.utils;

public class TimeConverter {
    public static long convertTimeToSeconds(String time) {
        String[] times = time.split("[:]+");
        return (((Integer.valueOf(times[0]) * 24) + Integer.valueOf(times[1]) * 60) + Integer.valueOf(times[2]) * 60);
    }
}
