package com.github.pinmacaroon.dchook.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CommandParser {
    private static final Pattern NON_APPEND = Pattern.compile("[^\"|^\\s]");

    public static List<Object> parseString(String source) {
        List<String> parsed = new ArrayList<>();
        StringBuilder holder = new StringBuilder();
        boolean capture = false;
        for (char i : source.toCharArray()) {
            if (!(i == '\'' || i == '"' || i == ' ')) {
                holder.append(i);
            } else if (i == '\'' || i == '"') {
                if (capture) {
                    capture = false;
                    parsed.add(holder.toString());
                    holder.setLength(0);
                } else {
                    parsed.add(holder.toString());
                    holder.setLength(0);
                    capture = true;
                }
            } else if (i == ' ') {
                if (capture) {
                    holder.append(i);
                } else {
                    parsed.add(holder.toString());
                    holder.setLength(0);
                }
            }
        }
        List<Object> scanned = new ArrayList<>();
        parsed.add(holder.toString());
        for (String i : parsed) {
            if (i.isEmpty()) {
                continue;
            }
            try {
                scanned.add(Float.valueOf(i));
            } catch (NumberFormatException e) {
                if (i.equals("true")) {
                    scanned.add(true);
                } else if (i.equals("false")) {
                    scanned.add(false);
                } else {
                    scanned.add(i.strip());
                }
            }
        }
        return scanned;
    }
}
