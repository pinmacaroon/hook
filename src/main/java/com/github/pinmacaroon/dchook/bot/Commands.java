package com.github.pinmacaroon.dchook.bot;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;

import java.util.List;

public class Commands {
    public static class Definition {
        public static final ApplicationCommandRequest LIST_COMMAND = ApplicationCommandRequest.builder()
                .name("list")
                .description("list people currently on the server")
                .build();
    }
    public static List<ApplicationCommandRequest> COMMANDS = List.of(
        Definition.LIST_COMMAND
    );

    public static void registerAll(GatewayDiscordClient client, long guild_id, long app_id){
        client.getRestClient().getApplicationService()
                .bulkOverwriteGuildApplicationCommand(app_id, guild_id, COMMANDS)
                .subscribe();
    }

    public static void handleCommands(GatewayDiscordClient client){
        client.on(ChatInputInteractionEvent.class, chatInputInteractionEvent -> {
            if(chatInputInteractionEvent.getCommandName().equals("list")) {
                //return ListPlayersCommand.handleCommand(chatInputInteractionEvent);
                return chatInputInteractionEvent.reply("not implemented").withEphemeral(true);
            }
            else return chatInputInteractionEvent.reply("invalid command").withEphemeral(true);
        }).subscribe();
    }
}
