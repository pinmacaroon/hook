package com.github.pinmacaroon.dchook.util;

public class TimeConverter {
    public static String timeOfDayToHoursMinutes(long time){
        return (int) Math.floor(time / 1000) + ":" + (int) ((time % 1000) / 1000.0 * 60);
    }
}
