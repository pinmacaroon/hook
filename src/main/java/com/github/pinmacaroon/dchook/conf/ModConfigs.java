package com.github.pinmacaroon.dchook.conf;

import com.github.pinmacaroon.dchook.Hook;
import com.mojang.datafixers.util.Pair;

import java.time.Instant;

public class ModConfigs {
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;

    public static String WEBHOOK_URL;
    public static String MESSAGES_SERVER_STARTING;
    public static String MESSAGES_SERVER_STOPPED;
    public static String MESSAGES_SERVER_STARTED;
    public static String MESSAGES_SERVER_STOPPING;
    public static boolean FUNCTIONS_ALLOWOOCMESSAGES;

    public static boolean MESSAGES_SERVER_STARTING_ALLOWED;
    public static boolean MESSAGES_SERVER_STOPPED_ALLOWED;
    public static boolean MESSAGES_SERVER_STARTED_ALLOWED;
    public static boolean MESSAGES_SERVER_STOPPING_ALLOWED;

    public static boolean FUNCTIONS_MODENABLED;
    public static boolean FUNCTIONS_PROMOTIONS_ENABLED;

    public static boolean FUNCTIONS_BOT_ENABLED;
    public static String FUNCTIONS_BOT_TOKEN;
    public static String FUNCTIONS_BOT_PREFIX;

    public static boolean FUNCTIONS_UPDATE;

    public static void registerConfigs() {
        configs = new ModConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of(Hook.MOD_ID).provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        configs.addDocumentationLine("Default config file generated with version " + Hook.VERSION + " at " + Instant.now().toString() + "!");
        configs.addBlankLine();

        configs.addDocumentationLine("Configure functionality of the mod:");
        configs.addKeyValuePair(new Pair<>("functions.mod_enabled", true), "enables/disables the mod's functionality");
        configs.addKeyValuePair(new Pair<>("functions.allow_ooc_messages", true), "allow players to be ignored from proxying if their message ends with double slashes?");
        configs.addKeyValuePair(new Pair<>("functions.promotions.enabled", true), "are tips and hints/promotion embeds allowed to be sent to Discord");
        configs.addKeyValuePair(new Pair<>("functions.bot.enabled", true), "is two-way chat (the bot) enabled?");
        configs.addKeyValuePair(new Pair<>("functions.bot.token", "TOKEN"), "bot token");
        configs.addKeyValuePair(new Pair<>("functions.bot.prefix", "$"), "bot command prefix");
        configs.addKeyValuePair(new Pair<>("functions.update", true), "check for updates");
        configs.addBlankLine();

        configs.addDocumentationLine("Configure Discord connection related parameters:");
        configs.addKeyValuePair(new Pair<>("webhook.url", "https://discord.com/api/webhooks/000/ABCDEF"), "url of webhook");
        configs.addBlankLine();

        configs.addDocumentationLine("Configure messages sent:");
        configs.addKeyValuePair(new Pair<>("messages.server.starting", "The server is starting!"), "start message");
        configs.addKeyValuePair(new Pair<>("messages.server.stopped", "The server has been stopped!"), "stop message");
        configs.addKeyValuePair(new Pair<>("messages.server.started", "The server has started!"), "opened/fully started message");
        configs.addKeyValuePair(new Pair<>("messages.server.stopping", "The server is stopping!"), "stopping message");
        configs.addKeyValuePair(new Pair<>("messages.server.starting.allowed", true), "start message allowed?");
        configs.addKeyValuePair(new Pair<>("messages.server.stopped.allowed", true), "stop message allowed?");
        configs.addKeyValuePair(new Pair<>("messages.server.started.allowed", true), "opened/fully started message allowed?");
        configs.addKeyValuePair(new Pair<>("messages.server.stopping.allowed", true), "stopping message allowed?");
        configs.addKeyValuePair(new Pair<>("messages.server.game.allowed", true), "default leave/join, advancement and death messages allowed?");

        configs.addBlankLine();
        configs.addDocumentationLine("Something didn't work? See the documentation or report an issue at this url: <" + Hook.DOCS_URL + ">!");
    }

    private static void assignConfigs() {
        WEBHOOK_URL = CONFIG.getOrDefault("webhook.url", "");
        MESSAGES_SERVER_STARTING = CONFIG.getOrDefault("messages.server.starting", "messages.server.starting");
        MESSAGES_SERVER_STARTED = CONFIG.getOrDefault("messages.server.started", "messages.server.started");
        MESSAGES_SERVER_STOPPED = CONFIG.getOrDefault("messages.server.stopped", "messages.server.stopped");
        MESSAGES_SERVER_STOPPING = CONFIG.getOrDefault("messages.server.stopping", "messages.server.stopping");
        FUNCTIONS_ALLOWOOCMESSAGES = CONFIG.getOrDefault("functions.allow_ooc_messages", false);

        MESSAGES_SERVER_STARTING_ALLOWED = CONFIG.getOrDefault("messages.server.starting.allowed", false);
        MESSAGES_SERVER_STARTED_ALLOWED = CONFIG.getOrDefault("messages.server.started.allowed", false);
        MESSAGES_SERVER_STOPPED_ALLOWED = CONFIG.getOrDefault("messages.server.stopped.allowed", false);
        MESSAGES_SERVER_STOPPING_ALLOWED = CONFIG.getOrDefault("messages.server.stopping.allowed", false);

        FUNCTIONS_MODENABLED = CONFIG.getOrDefault("functions.mod_enabled", true);
        FUNCTIONS_PROMOTIONS_ENABLED = CONFIG.getOrDefault("functions.promotions.enabled", false);
        //_ = CONFIG.getOrDefault("messages.server.game.allowed", false);

        FUNCTIONS_BOT_ENABLED = CONFIG.getOrDefault("functions.bot.enabled", false);
        FUNCTIONS_BOT_TOKEN = CONFIG.getOrDefault("functions.bot.token", "");
        FUNCTIONS_BOT_PREFIX = CONFIG.getOrDefault("functions.bot.prefix", "$");

        FUNCTIONS_UPDATE = CONFIG.getOrDefault("functions.update", false);

        System.out.println("all " + configs.getConfigsList().size() + " have been set properly");
    }
}
