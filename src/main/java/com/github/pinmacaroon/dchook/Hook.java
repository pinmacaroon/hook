package com.github.pinmacaroon.dchook;

import com.github.pinmacaroon.dchook.conf.ModConfigs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import net.fabricmc.api.DedicatedServerModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
//import org.json.simple.JSONObject;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class Hook implements DedicatedServerModInitializer {
	public static final String MOD_ID = "dchook";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static HttpClient HTTPCLIENT = HttpClient.newHttpClient();
	public static Gson GSON = new GsonBuilder().create();
	public static final String VERSION = "0.2.1";
	public static final String DOCS_URL = "https://modrinth.com/mod/dchook";
	public static final Pattern WEBHOOK_URL_PATTERN = Pattern.compile("^https:\\/\\/(ptb\\.|canary\\.)?discord\\.com\\/api\\/webhooks\\/\\d+\\/.+$");
	//public static HttpClient HTTPCLIENT = HttpClient.newHttpClient();

	@Override
	public void onInitializeServer() {
		//TODO new feature: timed info https://stackoverflow.com/questions/1784331/how-do-you-efficiently-repeat-an-action-every-x-minutes
		ModConfigs.registerConfigs();
		if(!ModConfigs.FUNCTIONS_MODENABLED){
			LOGGER.error("hook mod was explicitly told to not operate!");
			return;
		}
		if(!WEBHOOK_URL_PATTERN.matcher(ModConfigs.WEBHOOK_URL).find()){
			LOGGER.error("webhook url was not a valid discord api endpoint, thus the mod cant operate!");
			return;
		}
		HttpRequest get_webhook = HttpRequest.newBuilder()
						.GET()
						.uri(URI.create(
								ModConfigs.WEBHOOK_URL
						))
						.build();

		try {
			var response = HTTPCLIENT.send(get_webhook, HttpResponse.BodyHandlers.ofString());
			int status = response.statusCode();
			if(status != 200){
				LOGGER.error(
						"the webhook was not found or couldn't reach discord servers! discord said: '{}'",
						new JsonParser().parse(response.body()).getAsJsonObject().get("message").getAsString()
				);
				return;
			}
		} catch (InterruptedException | IOException e) {
			throw new RuntimeException(e);
        }

		LOGGER.info("all checks succeeded, starting webhook managing!");

		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			if (!ModConfigs.MESSAGES_SERVER_STARTING_ALLOWED) return;
			HashMap<String, String> requestbody = new HashMap<>();
			requestbody.put("content", ModConfigs.MESSAGES_SERVER_STARTING);
			requestbody.put("username", "server");


			HttpRequest post = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(requestbody)))
					.uri(URI.create(
							ModConfigs.WEBHOOK_URL
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
			if (!ModConfigs.MESSAGES_SERVER_STARTED_ALLOWED) return;
			HashMap<String, String> requestbody = new HashMap<>();
			requestbody.put("content", ModConfigs.MESSAGES_SERVER_STARTED);
			requestbody.put("username", "server");


			HttpRequest post = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(requestbody)))
					.uri(URI.create(
							ModConfigs.WEBHOOK_URL
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
			if (!ModConfigs.MESSAGES_SERVER_STOPPED_ALLOWED) return;
			HashMap<String, String> requestbody = new HashMap<>();
			requestbody.put("content", ModConfigs.MESSAGES_SERVER_STOPPED);
			requestbody.put("username", "server");


			HttpRequest post = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(requestbody)))
					.uri(URI.create(
							ModConfigs.WEBHOOK_URL
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

		ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
			if (!ModConfigs.MESSAGES_SERVER_STOPPING_ALLOWED) return;
			HashMap<String, String> requestbody = new HashMap<>();
			requestbody.put("content", ModConfigs.MESSAGES_SERVER_STOPPING);
			requestbody.put("username", "server");


			HttpRequest post = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(requestbody)))
					.uri(URI.create(
							ModConfigs.WEBHOOK_URL
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
			if(message.getSignedContent().strip().endsWith("//") && ModConfigs.FUNCTIONS_ALLOWOOCMESSAGES) return;

			HashMap<String, String> requestbody = new HashMap<>();
			requestbody.put("content", message.getSignedContent());
			requestbody.put("username", sender.getName().getString()); //TODO FIXXXXXXXXX
			requestbody.put("avatar_url", "https://crafthead.net/helm/" + message.getSender().toString());


			HttpRequest post = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(requestbody)))
					.uri(URI.create(
							ModConfigs.WEBHOOK_URL
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
			requestbody.put("content", Text.translatable(text.getString()).getString());
			requestbody.put("username", "game");


			HttpRequest post = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(requestbody)))
					.uri(URI.create(
							ModConfigs.WEBHOOK_URL
					))
					.header("Content-Type", "application/json")
					.build();
			//LOGGER.info(GSON.toJson(requestbody));

			try {
				//HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body();
				LOGGER.info(HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body());
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}

		});
	}
}