package com.github.pinmacaroon.dchook.bot.commands;

import com.github.pinmacaroon.dchook.Hook;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.world.GameRules;

import java.math.BigDecimal;

public class StatCommand {
    public static void run(SlashCommandInteractionEvent event) {
        StringBuilder list = new StringBuilder();
        list.append("Stats of this instance:\n```");

        list.append("uptime = %dmin%n".formatted(Hook.getGameServer().getTicks() / 20 / 60));
        list.append("keepinventory = %b%n".formatted(Hook.getGameServer().getGameRules().get(GameRules.KEEP_INVENTORY)
                .get()));
        list.append("mcversion = %s%n".formatted(Hook.getGameServer().getVersion()));
        list.append("fabricmcversion = %s%n".formatted(FabricLoaderImpl.VERSION));
        list.append("dchookversion = %s%n".formatted(Hook.VERSION));
        list.append("players = %d/%d%n".formatted(Hook.getGameServer().getCurrentPlayerCount(),
                Hook.getGameServer().getMaxPlayerCount()));
        list.append("memory = ~%smb%n".formatted(Math.rint(
                (double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024
        )));
        list.append("overworld_border = %s%n".formatted(
                BigDecimal.valueOf((int) Hook.getGameServer().getOverworld().getWorldBorder().getSize()/2)
                        .toPlainString()
        ));

        list.append("```");
        event.reply(list.toString()).setEphemeral(event.getOption("ephemeral", false, OptionMapping::getAsBoolean))
                .queue();
    }
}
