package com.github.pinmacaroon.dchook.conf;

import com.mojang.datafixers.util.Pair;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class ModConfigProvider implements SimpleConfig.DefaultConfig {

    private String configContents = "";

    public List<Pair> getConfigsList() {
        return configsList;
    }

    private final List<Pair> configsList = new ArrayList<>();

    public void addKeyValuePair(Pair<String, ?> keyValuePair, String comment) {
        configsList.add(keyValuePair);
        configContents +=
                "# " + comment + "\n" +
                "# default: " + keyValuePair.getSecond() + "\n" +
                keyValuePair.getFirst() + "=" + keyValuePair.getSecond() + "\n";
    }

    public void addDocumentationLine(String comment) {
        configContents += "# " + comment + "\n";
    }

    public void addBlankLine() {
        configContents += "\n";
    }

    @Override
    public String get(String namespace) {
        return configContents;
    }
}
