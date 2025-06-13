package com.github.pinmacaroon.dchook;

import com.github.pinmacaroon.dchook.bot.Bot;
import com.github.pinmacaroon.dchook.conf.ModConfigs;
import com.github.pinmacaroon.dchook.util.EventListeners;
import com.github.pinmacaroon.dchook.util.VersionChecker;
import com.github.zafarkhaja.semver.Version;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Random;
import java.util.regex.Pattern;

public class Hook implements DedicatedServerModInitializer {

    public static final String MOD_ID = "dchook";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final HttpClient HTTPCLIENT = HttpClient.newHttpClient();
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    public static final Version VERSION = new Version.Builder()
            .setMajorVersion(1)
            .setMinorVersion(0)
            .setPatchVersion(0)
            .setBuildMetadata("fabric")
            //.setPreReleaseVersion("alpha", "2")
            .build();
    public static final String DOCS_URL = "https://modrinth.com/mod/dchook";
    public static final Random RANDOM = new Random(Instant.now().getEpochSecond());
    @SuppressWarnings("RegExpRedundantEscape")
    public static final Pattern WEBHOOK_URL_PATTERN = Pattern.compile(
            "^https:\\/\\/(ptb\\.|canary\\.)?discord\\.com\\/api\\/webhooks\\/\\d+\\/.+$"
    );

    public static volatile Bot BOT;

    private static MinecraftServer MINECRAFT_SERVER;

    public static MinecraftServer getGameServer() {
        return MINECRAFT_SERVER;
    }

    public static void setMinecraftServer(MinecraftServer minecraftServer) {
        MINECRAFT_SERVER = minecraftServer;
    }

    @Override
    public void onInitializeServer() {
        ModConfigs.registerConfigs();

        if(!ModConfigs.FUNCTIONS_MODENABLED){
            LOGGER.error("hook mod was explicitly told to not operate!");
            return;
        }

        if(!WEBHOOK_URL_PATTERN.matcher(ModConfigs.WEBHOOK_URL).find()){
            LOGGER.error("webhook url was not a valid discord api endpoint, thus the mod cant operate!");
            return;
        }

        if(ModConfigs.FUNCTIONS_BOT_ENABLED){
            try {
                BOT = new Bot(ModConfigs.FUNCTIONS_BOT_TOKEN);
            } catch (Exception e){
                LOGGER.error("couldn't initialise bot, two way chat disabled");
                LOGGER.error("{}:{}", e.getClass().getName(), e.getMessage());
                return;
            }
        }

        try {
            HttpRequest get_webhook = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(ModConfigs.WEBHOOK_URL))
                    .build();

            HttpResponse<String> response = HTTPCLIENT.send(get_webhook, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            JsonObject body = JsonParser.parseString(response.body()).getAsJsonObject();
            if(status != 200){
                LOGGER.error(
                        "the webhook was not found or couldn't reach discord servers! discord said: '{}'",
                        body.get("message").getAsString()
                );
                return;
            }
            Thread bot_rutime_thread = new Thread(() -> {
                while (BOT == null) {
                    Thread.onSpinWait();
                }
                BOT.setGUILD_ID(body.get("guild_id").getAsLong());
                BOT.setCHANNEL_ID(body.get("channel_id").getAsLong());
            });
            bot_rutime_thread.start();
        } catch (Exception e) {
            LOGGER.error("{}:{}", e.getClass().getName(), e.getMessage());
            throw new RuntimeException(e);
        }

        if(ModConfigs.FUNCTIONS_UPDATE) VersionChecker.checkVersion();

        LOGGER.info("all checks succeeded, starting webhook managing! version: {}", VERSION);
        if(!ModConfigs.FUNCTIONS_PROMOTIONS_ENABLED){
            LOGGER.warn("promotions were disabled by config. please consider turning them back on to support the mod!");
        }

        EventListeners.registerEventListeners();
    }
}