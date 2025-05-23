package com.github.pinmacaroon.dchook;

import com.github.pinmacaroon.dchook.bot.Bot;
import com.github.pinmacaroon.dchook.conf.ModConfigs;
import com.github.pinmacaroon.dchook.util.PromotionProvider;
import com.github.pinmacaroon.dchook.util.WaypointParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class Hook implements DedicatedServerModInitializer {

	public static final String MOD_ID = "dchook";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final HttpClient HTTPCLIENT = HttpClient.newHttpClient();
	public static final Gson GSON = new GsonBuilder().create();
	public static final String VERSION = "0.3.0-alpha6+build.1";
	public static final String DOCS_URL = "https://modrinth.com/mod/dchook";
	public static final Random RANDOM = new Random(Instant.now().getEpochSecond());
	public static final Pattern WEBHOOK_URL_PATTERN = Pattern.compile(
			"^https:\\/\\/(ptb\\.|canary\\.)?discord\\.com\\/api\\/webhooks\\/\\d+\\/.+$"
	);

	public static volatile Bot BOT;
	public static Thread BOT_THREAD;

	public static MinecraftServer MINECRAFT_SERVER;

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
				BOT = new Bot(ModConfigs.FUNCTIONS_BOT_TOKEN, ModConfigs.FUNCTIONS_BOT_PREFIX.toCharArray()[0]);
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
				BOT_THREAD = BOT.start();
			});
			bot_rutime_thread.start();
		} catch (Exception e) {
			LOGGER.error("{}:{}", e.getClass().getName(), e.getMessage());
			throw new RuntimeException(e);
        }

		LOGGER.info("all checks succeeded, starting webhook managing!");
		if(!ModConfigs.FUNCTIONS_PROMOTIONS_ENABLED){
			LOGGER.warn("promotions were disabled by config. please consider turning them back on to support the mod!");
		}

		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			MINECRAFT_SERVER = server;
			if (!ModConfigs.MESSAGES_SERVER_STARTING_ALLOWED) return;
			HashMap<String, String> requestbody = new HashMap<>();
			requestbody.put("content", "**"+ModConfigs.MESSAGES_SERVER_STARTING+"**");
			requestbody.put("username", "server");


			HttpRequest post = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(requestbody)))
					.uri(URI.create(
							ModConfigs.WEBHOOK_URL
					))
					.header("Content-Type", "application/json")
					.build();

            try {
                HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }


        });

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			if (!ModConfigs.MESSAGES_SERVER_STARTED_ALLOWED) return;
			HashMap<String, String> requestbody = new HashMap<>();
			requestbody.put("content", "**"+ModConfigs.MESSAGES_SERVER_STARTED+"**");
			requestbody.put("username", "server");


			HttpRequest post = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(requestbody)))
					.uri(URI.create(
							ModConfigs.WEBHOOK_URL
					))
					.header("Content-Type", "application/json")
					.build();

			try {
				//LOGGER.info(HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body());
				HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body();
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}
			if(ModConfigs.FUNCTIONS_PROMOTIONS_ENABLED){
				PromotionProvider.sendAutomaticPromotion(URI.create(
						ModConfigs.WEBHOOK_URL
				));
			}

		});

		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			if (!ModConfigs.MESSAGES_SERVER_STOPPED_ALLOWED) return;
			HashMap<String, String> requestbody = new HashMap<>();
			requestbody.put("content", "**"+ModConfigs.MESSAGES_SERVER_STOPPED+"**");
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
			if(BOT != null){
				BOT.stop();
			}
		});

		ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
			if (!ModConfigs.MESSAGES_SERVER_STOPPING_ALLOWED) return;
			HashMap<String, String> requestbody = new HashMap<>();
			requestbody.put("content", "**"+ModConfigs.MESSAGES_SERVER_STOPPING+"**");
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
			if(WaypointParser.isWaypoint(message.getSignedContent())){
				List<Object> list = WaypointParser.constructWaypointFromString(message.getSignedContent());
				requestbody.put("content", MessageFormat.format(
						"*Shared a waypoint called *{0} ({1})* at `x={2} y={3} z={4}` from {5}!*",
						list.get(0),
						list.get(1),
						Array.get(list.get(2), 0),
						Array.get(list.get(2), 1),
						Array.get(list.get(2), 2),
						list.get(3)
				));
			} else requestbody.put("content", message.getSignedContent());
			requestbody.put("username", sender.getName().getString());
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
			if(Text.translatable(text.getString()).getString().startsWith("<")) return;
			HashMap<String, String> requestbody = new HashMap<>();
			requestbody.put("content", "**"+Text.translatable(text.getString()).getString()+"**");
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
				HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body();
				//LOGGER.info(HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get().body());
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}

		});
	}
}