package com.github.pinmacaroon.dchook;

import com.github.pinmacaroon.dchook.conf.ModConfigs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.DedicatedServerModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
//import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class Hook implements DedicatedServerModInitializer {
	public static final String MOD_ID = "dchook";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static HttpClient HTTPCLIENT = HttpClient.newHttpClient();
	public static Gson GSON = new GsonBuilder().create();
	//public static HttpClient HTTPCLIENT = HttpClient.newHttpClient();

	@Override
	public void onInitializeServer() {
		//https://ptb.discord.com/api/webhooks/1353003817343520829/PPHkqRJIR_Dxbg24-94jnkuBGQqh1jJV1BxzpE3-YKM-mC0kLM18fEgByVnU69DGBBL6
		ModConfigs.registerConfigs();
        LOGGER.info("intiting hook mod after config loaded! webhook id is '{}'!", ModConfigs.WEBHOOKID);

		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			HashMap<String, String> requestbody = new HashMap<>();
			requestbody.put("content", ModConfigs.MESSAGESSERVERSTART);
			requestbody.put("username", "server");


			HttpRequest post = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(requestbody)))
					.uri(URI.create(
							"https://discord.com/api/webhooks/" + ModConfigs.WEBHOOKID + "/" + ModConfigs.WEBHOOKTOKEN
					))
					.header("Content-Type", "application/json")
					.build();
			//LOGGER.info(JSONObject.toJSONString(requestbody));

            try {
                //LOGGER.info(HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body());
                HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			HashMap<String, String> requestbody = new HashMap<>();
			requestbody.put("content", ModConfigs.MESSAGESSERVEROPEN);
			requestbody.put("username", "server");


			HttpRequest post = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(requestbody)))
					.uri(URI.create(
							"https://discord.com/api/webhooks/" + ModConfigs.WEBHOOKID + "/" + ModConfigs.WEBHOOKTOKEN
					))
					.header("Content-Type", "application/json")
					.build();
			//LOGGER.info(JSONObject.toJSONString(requestbody));

			try {
				//LOGGER.info(HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body());
				HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body();
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}
		});

		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			HashMap<String, String> requestbody = new HashMap<>();
			requestbody.put("content", ModConfigs.MESSAGESSERVERSTOP);
			requestbody.put("username", "server");


			HttpRequest post = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(requestbody)))
					.uri(URI.create(
							"https://discord.com/api/webhooks/" + ModConfigs.WEBHOOKID + "/" + ModConfigs.WEBHOOKTOKEN
					))
					.header("Content-Type", "application/json")
					.build();
			//LOGGER.info(JSONObject.toJSONString(requestbody));

			try {
				//LOGGER.info(HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body());
				HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body();
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}
		});

		ServerMessageEvents.CHAT_MESSAGE.register((message, sender, parameters) -> {
			HashMap<String, String> requestbody = new HashMap<>();
			requestbody.put("content", message.getSignedContent());
			requestbody.put("username", sender.getName().getLiteralString());
			requestbody.put("avatar_url", "https://crafthead.net/helm/" + message.getSender().toString());


			HttpRequest post = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(requestbody)))
					.uri(URI.create(
							"https://discord.com/api/webhooks/" + ModConfigs.WEBHOOKID + "/" + ModConfigs.WEBHOOKTOKEN
					))
					.header("Content-Type", "application/json")
					.build();
			//LOGGER.info(JSONObject.toJSONString(requestbody));

			try {
				//LOGGER.info(HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body());
				HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body();
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}
		});

		ServerMessageEvents.GAME_MESSAGE.register((server, text, b) -> {
			HashMap<String, String> requestbody = new HashMap<>();
			requestbody.put("content", text.getString());
			requestbody.put("username", "game");


			HttpRequest post = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(requestbody)))
					.uri(URI.create(
							"https://discord.com/api/webhooks/" + ModConfigs.WEBHOOKID + "/" + ModConfigs.WEBHOOKTOKEN
					))
					.header("Content-Type", "application/json")
					.build();
			//LOGGER.info(JSONObject.toJSONString(requestbody));

			try {
				HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body();
				//LOGGER.info(HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body());
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}

		});
	}
}