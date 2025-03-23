package com.github.pinmacaroon.dchook.conf;

import com.github.pinmacaroon.dchook.Hook;
import com.mojang.datafixers.util.Pair;

import java.math.BigInteger;

public class ModConfigs {
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;

    public static String WEBHOOKTOKEN;
    public static String WEBHOOKID;
    public static String MESSAGESSERVERSTART;
    public static String MESSAGESSERVERSTOP;
    public static String MESSAGESSERVEROPEN;
    public static String MESSAGESSERVERSTOPPING;

    public static void registerConfigs() {
        configs = new ModConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of(Hook.MOD_ID + "config").provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        configs.addKeyValuePair(new Pair<>("webhook.id", "000000000"), "id of webhook");
        configs.addKeyValuePair(new Pair<>("webhook.token", "LiGmA-Ba115"), "token of webhook (please keep it safe :3)");
        configs.addKeyValuePair(new Pair<>("messages.server.start", "the server is starting!"), "start message");
        configs.addKeyValuePair(new Pair<>("messages.server.stop", "the server has been stopped!"), "stop message");
        configs.addKeyValuePair(new Pair<>("messages.server.open", "the server is online!"), "opened/fully started message");
        configs.addKeyValuePair(new Pair<>("messages.server.stopping", "the server is stopping!"), "stopped msg");
    }

    private static void assignConfigs() {
        WEBHOOKID = CONFIG.getOrDefault("webhook.id", "0");
        WEBHOOKTOKEN = CONFIG.getOrDefault("webhook.token", "null");
        MESSAGESSERVERSTART = CONFIG.getOrDefault("messages.server.start", "null");
        MESSAGESSERVEROPEN = CONFIG.getOrDefault("messages.server.open", "null");
        MESSAGESSERVERSTOP = CONFIG.getOrDefault("messages.server.stop", "null");
        MESSAGESSERVERSTOPPING = CONFIG.getOrDefault("messages.server.stopping", "null");

        System.out.println("all " + configs.getConfigsList().size() + " have been set properly");
    }
}
