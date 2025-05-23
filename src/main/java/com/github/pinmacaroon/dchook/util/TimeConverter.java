package com.github.pinmacaroon.dchook.util;

public class TimeConverter {
    public static String timeOfDayToHoursMinutes(long time){
        return (int) Math.floor((double) time / 1000) + ":" + (int) ((time % 1000) / 1000.0 * 60);
    }
}
