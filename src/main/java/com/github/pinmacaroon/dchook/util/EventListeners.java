package com.github.pinmacaroon.dchook.util;

import com.github.pinmacaroon.dchook.Hook;
import com.github.pinmacaroon.dchook.conf.ModConfigs;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.text.Text;

import java.lang.reflect.Array;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EventListeners {
    public static void registerEventListeners(){
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            Hook.setMinecraftServer(server);
            if (!ModConfigs.MESSAGES_SERVER_STARTING_ALLOWED) return;

            HashMap<String, String> request_body = new HashMap<>();
            request_body.put("content", "**"+ModConfigs.MESSAGES_SERVER_STARTING+"**");
            request_body.put("username", "server");

            HttpRequest post = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(Hook.GSON.toJson(request_body)))
                    .uri(URI.create(ModConfigs.WEBHOOK_URL))
                    .header("Content-Type", "application/json")
                    .build();

            try {
                Hook.HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            if (!ModConfigs.MESSAGES_SERVER_STARTED_ALLOWED) return;

            HashMap<String, String> request_body = new HashMap<>();
            request_body.put("content", "**"+ModConfigs.MESSAGES_SERVER_STARTED+"**");
            request_body.put("username", "server");

            HttpRequest post = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(Hook.GSON.toJson(request_body)))
                    .uri(URI.create(ModConfigs.WEBHOOK_URL))
                    .header("Content-Type", "application/json")
                    .build();

            try {
                Hook.HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }

            if (ModConfigs.FUNCTIONS_PROMOTIONS_ENABLED) {
                PromotionProvider.sendAutomaticPromotion(URI.create(ModConfigs.WEBHOOK_URL));
            }
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            if (!ModConfigs.MESSAGES_SERVER_STOPPED_ALLOWED) return;

            HashMap<String, String> request_body = new HashMap<>();
            request_body.put("content", "**"+ModConfigs.MESSAGES_SERVER_STOPPED+"**");
            request_body.put("username", "server");

            HttpRequest post = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(Hook.GSON.toJson(request_body)))
                    .uri(URI.create(ModConfigs.WEBHOOK_URL))
                    .header("Content-Type", "application/json")
                    .build();

            try {
                Hook.HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }

            if(Hook.BOT != null) Hook.BOT.stop();
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            if (!ModConfigs.MESSAGES_SERVER_STOPPING_ALLOWED) return;

            HashMap<String, String> request_body = new HashMap<>();
            request_body.put("content", "**"+ModConfigs.MESSAGES_SERVER_STOPPING+"**");
            request_body.put("username", "server");

            HttpRequest post = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(Hook.GSON.toJson(request_body)))
                    .uri(URI.create(ModConfigs.WEBHOOK_URL))
                    .header("Content-Type", "application/json")
                    .build();

            try {
                Hook.HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        ServerMessageEvents.CHAT_MESSAGE.register((message, sender, parameters) -> {
            if(message.getSignedContent().strip().endsWith("//") && ModConfigs.FUNCTIONS_ALLOWOOCMESSAGES) return;

            HashMap<String, String> request_body = new HashMap<>();

            if(WaypointParser.isWaypoint(message.getSignedContent())){
                List<Object> list = WaypointParser.constructWaypointFromString(message.getSignedContent());
                request_body.put("content", MessageFormat.format(
                        "*Shared a waypoint called **{0} ({1})** at `x={2} y={3} z={4}` from {5}!*",
                        list.get(0),
                        list.get(1),
                        Array.get(list.get(2), 0),
                        Array.get(list.get(2), 1),
                        Array.get(list.get(2), 2),
                        list.get(3)
                ));
            } else request_body.put("content", MarkdownSanitizer.escape(message.getSignedContent()));

            request_body.put("username", sender.getName().getString());
            request_body.put("avatar_url", "https://crafthead.net/helm/" + message.getSender().toString());

            HttpRequest post = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(Hook.GSON.toJson(request_body)))
                    .uri(URI.create(ModConfigs.WEBHOOK_URL))
                    .header("Content-Type", "application/json")
                    .build();

            try {
                Hook.HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        ServerMessageEvents.GAME_MESSAGE.register((server, text, b) -> {
            if(Text.translatable(text.getString()).getString().startsWith("<")) return;

            HashMap<String, String> request_body = new HashMap<>();
            request_body.put("content", "**"+Text.translatable(text.getString()).getString()+"**");
            request_body.put("username", "game");

            HttpRequest post = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(Hook.GSON.toJson(request_body)))
                    .uri(URI.create(ModConfigs.WEBHOOK_URL))
                    .header("Content-Type", "application/json")
                    .build();

            try {
                Hook.HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
