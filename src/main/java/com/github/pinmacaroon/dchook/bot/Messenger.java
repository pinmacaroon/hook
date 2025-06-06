package com.github.pinmacaroon.dchook.bot;

import com.github.pinmacaroon.dchook.Hook;
import com.github.pinmacaroon.dchook.conf.ModConfigs;
import com.github.pinmacaroon.dchook.util.TimeConverter;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.discordjson.json.MessageCreateRequest;
import discord4j.rest.entity.RestChannel;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModEnvironment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// im not fixing this shit, this is Pandora's box, don't touch it. thanks be to the gods of Olympus for allowing this
// forsaken class to operate

public class Messenger {
    public static final Pattern MENTION_PATTERN = Pattern.compile("<(@)(\\d+)>");
    //TODO fix role pattern not working
    public static final Pattern ROLE_PATTERN = Pattern.compile("<(@&)(\\d+)>");
    public static final Pattern CHANNEL_PATTERN = Pattern.compile("<(#)(\\d+)>");
    public static final Pattern EMOJI_PATTERN = Pattern.compile("<a?:([A-Za-z0-9_]{2,}):(\\d+)>");

    public static void handeMessage(MessageCreateEvent messageCreateEvent, Bot bot) {
        if (messageCreateEvent.getMessage().getUserMentions().contains(bot.getGATEWAY_CLIENT().getSelf().block())) {
            RestChannel restChannel = bot.getGATEWAY_CLIENT().rest()
                    .getChannelById(messageCreateEvent.getMessage().getChannelId());
            restChannel.createMessage(String.format("-# Heya, my current prefix is `%s`!",
                            bot.getPREFIX()
                    )
            ).block();
        }
        if (messageCreateEvent.getMessage().getContent()
                .strip()
                .startsWith(Character.toString(bot.getPREFIX()))
        ) {
            List<Object> arguments = CommandParser.parseString(messageCreateEvent.getMessage().getContent().strip());
            String prefix = Character.toString(bot.getPREFIX());
            Message message = messageCreateEvent.getMessage();
            RestChannel restChannel = bot.getGATEWAY_CLIENT().rest().getChannelById(message.getChannelId());

            if (arguments.get(0).equals(prefix + "test")) {
                long size;
                try {
                    size = Files.walk(Hook.getMinecraftServer().getRunDirectory().toPath())
                            .filter(p -> p.toFile().isFile())
                            .mapToLong(p -> p.toFile().length())
                            .sum()/1024/1024;
                } catch (IOException e) {
                    size = 0L;
                }
                restChannel.createMessage(String.format("""
                                        The test was successful! `v:%s,m:%dmb/%dmb,g:%s,IP: %s:%d,s:%dmb`""",
                                Hook.VERSION, (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1024/1024,
                                Runtime.getRuntime().totalMemory()/1024/1024,
                                Hook.getMinecraftServer().getVersion(),
                                Hook.getMinecraftServer().getServerIp(), Hook.getMinecraftServer().getServerPort(),
                                size
                        )
                ).block();
                return;
            } else if (arguments.get(0).equals(prefix + "list") || arguments.get(0).equals(prefix + "l")) {
                StringBuilder player_list = new StringBuilder();
                player_list.append("""
                        There are currently **%d**/%d players online:\s""".formatted(
                        Hook.getMinecraftServer().getPlayerManager().getCurrentPlayerCount(),
                        Hook.getMinecraftServer().getPlayerManager().getMaxPlayerCount()
                ));
                Hook.getMinecraftServer().getPlayerManager().getPlayerList().forEach(
                        serverPlayerEntity ->
                                player_list.append("`").append(serverPlayerEntity.getName().getString()).append("` ")
                );
                restChannel.createMessage(player_list.toString()).block();
                return;
            } else if (arguments.get(0).equals(prefix + "time") || arguments.get(0).equals(prefix + "weather")) {
                String response = "The current in-game time in the overworld is **" +
                        TimeConverter.timeOfDayToHoursMinutes2(Hook.getMinecraftServer().getOverworld().getTimeOfDay()) +
                        "**! The weather is " +
                        ((Hook.getMinecraftServer().getOverworld().isRaining()) ? "rainy" : "clear") +
                        "! " +
                        ((Hook.getMinecraftServer().getOverworld().isThundering()) ? "It is thundering!" : "");
                restChannel.createMessage(response).block();
                return;
            } else if (arguments.get(0).equals(prefix + "mods")) {
                StringBuilder mod_list = new StringBuilder();
                AtomicInteger mods = new AtomicInteger();
                FabricLoader.getInstance().getAllMods().forEach(modContainer -> {
                    if (modContainer.getMetadata().getEnvironment() == ModEnvironment.UNIVERSAL
                            && !modContainer.getMetadata().getType().equals("builtin")
                            && !modContainer.getMetadata().getId().startsWith("fabric")
                            && !modContainer.getMetadata().getId().equals("mixinextras")
                            && modContainer.getContainingMod().isEmpty()) {
                        mod_list.append(modContainer.getMetadata().getName()).append(' ')
                                .append(modContainer.getMetadata().getVersion().getFriendlyString()).append('\n');
                        mods.getAndIncrement();
                    }
                });
                String response;
                if (mods.get() == 0) {
                    response = "The server currently has no required mods, you can join with a vanilla client!";
                } else {
                    response = "The server currently has " + mods.get() + " required mods:\n" + mod_list;
                }
                if (response.length() > 2000) {
                    response = response.substring(0, 1995) + "[...]";
                }
                restChannel.createMessage(response).block();
                return;
            } else if (arguments.get(0).equals(prefix + "help")) {
                String response = """
                        Every command needs the prefix before its name!
                        * `list`, aka `l`: list online players
                        * `time`, aka `weather`: get current overworld time and weather info
                        * `mods`: list all the required mods in the server (might be inaccurate)
                        * `test`: idk, testing command
                        * `skin <username> [['avatar'|'helm'|'cube'|'body'|'bust'] ['true'|'false']]`: get the skin of \
                        a given user. optional: select from a list of modes, and toggle outer layer""";
                restChannel.createMessage(response).block();
                return;
            } else if (arguments.get(0).equals(prefix + "skin") && arguments.size() >= 2) {
                MessageCreateRequest data;
                if (arguments.size() >= 4) {
                    if (arguments.get(2) == "helm" || arguments.get(2) == "avatar") {
                        data = MessageCreateRequest.builder()
                                .content("Skin of " + arguments.get(1).toString() + ":")
                                .addEmbed(EmbedCreateSpec.builder()
                                        .image("https://crafthead.net/" + arguments.get(2) + "/" + arguments.get(1).toString())
                                        .footer(
                                                "get a closer look at https://pinmacaroon.github.io/skinfetch/index.html", "")
                                        .build().asRequest())
                                .build();
                    } else {
                        data = MessageCreateRequest.builder()
                                .content("Skin of " + arguments.get(1).toString() + ":")
                                .addEmbed(EmbedCreateSpec.builder()
                                        .image("https://crafthead.net/" + (((boolean) arguments.get(3)) ? "armor/" : "")
                                                + arguments.get(2) + "/" + arguments.get(1).toString())
                                        .footer(
                                                "get a closer look at https://pinmacaroon.github.io/skinfetch/index.html", "")
                                        .build().asRequest())
                                .build();
                    }
                } else {
                    data = MessageCreateRequest.builder()
                            .content("Skin of " + arguments.get(1).toString() + ":")
                            .addEmbed(EmbedCreateSpec.builder()
                                    .image("https://crafthead.net/skin/" + arguments.get(1).toString())
                                    .footer(
                                            "get a closer look at https://pinmacaroon.github.io/skinfetch/index.html", "")
                                    .build().asRequest())
                            .build();
                }
                restChannel.createMessage(data).block();
                return;
            }
            return;
        }
        if ((messageCreateEvent.getMessage().getChannelId().asLong() == bot.getCHANNEL_ID())
                && (Hook.getMinecraftServer() != null)) {
            if (messageCreateEvent.getMessage().getAuthorAsMember().block() == null) {
                return;
            }

            if (
                    messageCreateEvent.getMessage().getAuthor().isPresent()
                            && messageCreateEvent.getMessage().getAuthor().get().isBot()) {
                return;
            }

            String raw_message = messageCreateEvent.getMessage().getContent();
            if (raw_message.endsWith("//") && ModConfigs.FUNCTIONS_ALLOWOOCMESSAGES) {
                return;
            }
            Matcher matcher = MENTION_PATTERN.matcher(raw_message);
            int end = 0;
            while (matcher.find(end)) {
                try {
                    String username = messageCreateEvent.getGuild().block(Duration.ofSeconds(2))
                            .getMemberById(Snowflake.of(matcher.group(2)))
                            .block(Duration.ofSeconds(2)).getUsername();
                    raw_message = matcher.replaceFirst("[@" + username + "]");
                    end = matcher.end();
                } catch (Exception e) {
                    e.printStackTrace();
                    raw_message = matcher.replaceFirst("[@unknown_user]");
                    end = matcher.end();
                }
            }

            matcher = CHANNEL_PATTERN.matcher(raw_message);
            end = 0;
            while (matcher.find(end)) {
                try {
                    String channel_name = messageCreateEvent.getGuild().block(Duration.ofSeconds(2))
                            .getChannelById(Snowflake.of(matcher.group(2)))
                            .block(Duration.ofSeconds(2)).getName();
                    raw_message = matcher.replaceFirst("[#" + channel_name + "]");
                    end = matcher.end();
                } catch (Exception e) {
                    e.printStackTrace();
                    raw_message = matcher.replaceFirst("[#unknown_channel]");
                    end = matcher.end();
                }
            }

            matcher = EMOJI_PATTERN.matcher(raw_message);
            end = 0;
            while (matcher.find(end)) {
                try {
                    String emoji_name = matcher.group(2);
                    raw_message = matcher.replaceFirst(":" + emoji_name + ":");
                    end = matcher.end();
                } catch (Exception e) {
                    e.printStackTrace();
                    raw_message = matcher.replaceFirst(":unknown_emoji:");
                    end = matcher.end();
                }
            }

            /* TODO fix!!!!
            matcher = ROLE_PATTERN.matcher(raw_message);
            end = 0;
            while (matcher.find(end)) {
                try {
                    String role_name = messageCreateEvent.getGuild().block(Duration.ofSeconds(2))
                            .getRoleById(Snowflake.of(matcher.group(2)))
                            .block(Duration.ofSeconds(2)).getName();
                    raw_message = matcher.replaceFirst("[@" + role_name + "]");
                    end = matcher.end();
                } catch (Exception e) {
                    e.printStackTrace();
                    raw_message = matcher.replaceFirst("[@unknown_role]");
                    end = matcher.end();
                }
            }
             */
            //temp fix so that chat doesn't look ugly
            matcher = ROLE_PATTERN.matcher(raw_message);
            end = 0;
            while (matcher.find(end)) {
                raw_message = matcher.replaceFirst("[@role]");
                end = matcher.end();
            }

            MutableText signature;
            if (messageCreateEvent.getMessage().getMessageReference().isPresent()) {
                if (messageCreateEvent.getMessage().getReferencedMessage().get().getAuthor().isEmpty()
                        && messageCreateEvent.getMessage().getReferencedMessage().get().getWebhook().block().getName()
                        .isPresent()) {
                    signature = Text.literal(
                            "<" +
                                    messageCreateEvent.getMessage().getReferencedMessage().get()
                                            .getWebhook().block().getName().get() + "> -> " +
                                    "<@" + messageCreateEvent.getMessage().getAuthorAsMember().block().getUsername() + ">: "
                    );
                } else {
                    signature = Text.literal(
                            "<@" +
                                    messageCreateEvent.getMessage().getReferencedMessage().get()
                                            .getAuthor().get().getUsername() + "> -> " +
                                    "<@" + messageCreateEvent.getMessage().getAuthorAsMember().block().getUsername() + ">: "
                    );
                }
            } else {
                signature = Text.literal(
                        "<@" + messageCreateEvent.getMessage().getAuthorAsMember().block().getUsername() + ">: "
                );
            }
            MutableText content = Text.literal(
                    (raw_message.isBlank())
                            ? "<image>" : raw_message
            );
            MutableText formatted = signature.formatted(Formatting.BLUE)
                    .append(content);
            Hook.getMinecraftServer().getPlayerManager().broadcast(formatted, false);
        }
    }
}
