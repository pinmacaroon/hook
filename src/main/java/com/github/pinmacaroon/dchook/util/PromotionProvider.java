package com.github.pinmacaroon.dchook.util;

import com.github.pinmacaroon.dchook.Hook;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

import static com.github.pinmacaroon.dchook.Hook.*;

public class PromotionProvider {
    public static JsonObject getMcfetchPromotion(){
        JsonObject embed = new JsonObject();

        embed.addProperty("url", "https://pinmacaroon.github.io/mcfetch/legacy/index.html");

        embed.addProperty("type", "rich");

        embed.addProperty("title", "mcfetch: see minecraft server stats without starting the game!");

        embed.addProperty("description", "a **simple**, **easy to use** and **minimal tool** " +
                "to see who are online on your favorite servers! **no download** needed, **no login** needed! " +
                "**no ads** and no restrictions!\nall you need to get server information is the **IP** address " +
                "of the server!");

        embed.addProperty("color", 15448355);

        JsonArray fields = new JsonArray();
        JsonObject field1 = new JsonObject();
        field1.addProperty("value", "start using it right now at this link: " +
                "<https://pinmacaroon.github.io/mcfetch/legacy/index.html>");
        field1.addProperty("name", "interested?");
        fields.add(field1);
        embed.add("fields", fields);

        JsonObject author = new JsonObject();
        author.addProperty("name", "mcfetch");
        author.addProperty("url", "https://pinmacaroon.github.io/mcfetch/legacy/index.html");
        embed.add("author", author);

        embed.addProperty("timestamp", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));

        JsonObject footer = new JsonObject();
        footer.addProperty("text", "this was a promotion of first-party services or a tip message! " +
                "if you wish to turn this off, go to the configuration file!");
        embed.add("footer", footer);
        return embed;
    }

    public static JsonObject getSequoiaPromotion(){
        JsonObject embed = new JsonObject();

        embed.addProperty("url", "https://pinmacaroon.github.io/mcfetch/legacy/index.html");

        embed.addProperty("type", "rich");

        embed.addProperty("title", "Sequoia: Adds a really nice biome with some really tall trees!");

        embed.addProperty("description", "A mod that adds the amazing sequoia forests! Custom " +
                "tree types, blocks, structures!");

        embed.addProperty("color", 15448355);

        JsonArray fields = new JsonArray();

        JsonObject field1 = new JsonObject();
        field1.addProperty("value", """
                Full block set like you would expect:
                * Log and Stripped Log
                * Wood and Stripped Wood
                * Planks
                * etc...""");
        field1.addProperty("name", "Sequoia wood type");
        fields.add(field1);

        JsonObject field2 = new JsonObject();
        field2.addProperty("value", """
                Fresh air, big trees. Really beautiful place to set up camp.\
                 You can even find berry bushes! You can find three types of sequoia trees:
                * small
                * medium size
                * huge (structure, to be added)""");
        field2.addProperty("name", "Sequoia forest");
        fields.add(field2);

        embed.add("fields", fields);

        JsonObject author = new JsonObject();
        author.addProperty("name", "Sequoia");
        author.addProperty("url", "https://modrinth.com/mod/sequoia-pine");
        author.addProperty("icon_url", "https://cdn.modrinth.com/data/GYYIncFH/2fabee293b4be237825df51" +
                "defe3b977058d31e7.png");
        embed.add("author", author);

        JsonObject image = new JsonObject();
        image.addProperty("url", "https://cdn.modrinth.com/data/GYYIncFH/images/5dd0848d40469d0dd4767" +
                "c73ac926e112978645d.jpeg");
        embed.add("image", image);

        embed.addProperty("timestamp", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));

        JsonObject footer = new JsonObject();
        footer.addProperty("text", "this was a promotion of first-party services or a tip message! " +
                "if you wish to turn this off, go to the configuration file!");

        embed.add("footer", footer);
        return embed;
    }

    public static JsonObject getOocMessageTip(){
        JsonObject embed = new JsonObject();

        embed.addProperty("type", "rich");

        embed.addProperty("title", "out of character messages");

        embed.addProperty("description", "make messages hidden from the mod by ending it with `//`!" +
                " it wont get transferred to Discord! you can configure this in the config file!");

        JsonObject author = new JsonObject();
        author.addProperty("name", "tips and hints");
        embed.add("author", author);

        embed.addProperty("timestamp", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));

        JsonObject footer = new JsonObject();
        footer.addProperty("text", "this was a promotion of first-party services or a tip message! " +
                "if you wish to turn this off, go to the configuration file!");
        embed.add("footer", footer);
        return embed;
    }

    public static JsonObject getDocumentationTip(){
        JsonObject embed = new JsonObject();

        embed.addProperty("type", "rich");

        embed.addProperty("title", "documentation");

        embed.addProperty("url", "https://pinmacaroon.github.io/hook/docs.html");

        embed.addProperty("description", """
                oh noes! you couldn't figure out how to use the mod? i got your back! go to
                 <https://pinmacaroon.github.io/hook/docs.html> and see the table with the configuration keys and
                 values! this page will be your hub for updates and information concerning the mod.""");

        JsonObject author = new JsonObject();
        author.addProperty("name", "tips and hints");
        embed.add("author", author);

        embed.addProperty("timestamp", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));

        JsonObject image = new JsonObject();
        image.addProperty("url", "https://c.tenor.com/BRBnJitMZBYAAAAd/tenor.gif");
        embed.add("image", image);

        JsonObject footer = new JsonObject();
        footer.addProperty("text", "this was a promotion of first-party services or a tip message! " +
                "if you wish to turn this off, go to the configuration file!");
        embed.add("footer", footer);
        return embed;
    }

    public static JsonObject getHydrationTip(){
        JsonObject embed = new JsonObject();

        embed.addProperty("type", "rich");

        embed.addProperty("title", "hydration");

        embed.addProperty("description", """
                **did you drink water today? do you drink enough?**
                an average daily "total water"\
                 intake of 3 liters is recommended by the USDA (this includes water from non-drinking water sources,\
                 like foods). specifically, **eight cups of drinking water a day** is the amount recommended by most\
                 nutritionists.""");

        JsonObject author = new JsonObject();
        author.addProperty("name", "tips and hints");
        embed.add("author", author);

        embed.addProperty("timestamp", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));

        JsonObject footer = new JsonObject();
        footer.addProperty("text", "this was a promotion of first-party services or a tip message! " +
                "if you wish to turn this off, go to the configuration file!");
        embed.add("footer", footer);
        return embed;
    }

    /**
     * @param embeds {@link JsonArray} of {@link JsonObject} which are discord embeds
     * @param webhook {@link URI} of the webhook api endpoint
     */
    public static void sendPromotion(JsonArray embeds, URI webhook){

        JsonObject request_body = new JsonObject();
        request_body.addProperty("username", "promotion");
        request_body.add("embeds", embeds);

        HttpRequest post = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(request_body.toString()))
                .uri(webhook)
                .header("Content-Type", "application/json")
                .build();

        try {
            var response = HTTPCLIENT.sendAsync(post, HttpResponse.BodyHandlers.ofString()).get();
            LOGGER.debug(String.valueOf(response.statusCode()));
            LOGGER.debug(request_body.toString());
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.warn(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    /**
     * @return {@link JsonObject} of the single promotion embed, or a {@code null} if the gods decide that the rng shall be more than 4
     */
    public static @Nullable JsonObject automaticPromotionSelector(){
        int id = Hook.RANDOM.nextInt(0, 5);
        LOGGER.debug(String.valueOf(id));
        return switch (id) {
            case 0 -> getMcfetchPromotion();
            case 1 -> getOocMessageTip();
            case 2 -> getSequoiaPromotion();
            case 3 -> getDocumentationTip();
            case 4 -> getHydrationTip();
            default -> null;
        };
    }

    /**
     * @param webhook {@link URI} of the webhook api endpoint
     */
    public static void sendAutomaticPromotion(URI webhook){
        JsonObject promotion = automaticPromotionSelector();
        if(promotion == null) return;
        JsonArray promotions = new JsonArray();
        promotions.add(promotion);
        sendPromotion(promotions, webhook);
    }
}
