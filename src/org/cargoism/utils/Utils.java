package org.cargoism.utils;

public class Utils {

    public static final int MINUTES_IN_HOUR = 60;

    public static final int HOURS_IN_DAY = 24;

    public static final int MINUTES_IN_DAY = MINUTES_IN_HOUR * HOURS_IN_DAY;

    public static String padLeftZeroes(String string, int length) {
        return String.format("%1$" + length + "s", string).replace(' ', '0');
    }

    public static int minutesToDays(int minutes) {
        return minutes / MINUTES_IN_DAY;
    }

    public static int minutesToHours(int minutes) {
        return minutes / MINUTES_IN_HOUR % HOURS_IN_DAY;
    }


    public static String formatTime(int minutes) {
        return padLeftZeroes(String.valueOf(minutesToDays(minutes)), 2) + ":"
                + padLeftZeroes(String.valueOf(minutesToHours(minutes)), 2) + ":"
                + padLeftZeroes(String.valueOf(minutes % 60), 2);
    }

}
