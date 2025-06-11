package com.github.pinmacaroon.dchook.bot.event;

import com.github.pinmacaroon.dchook.bot.Bot;
import com.github.pinmacaroon.dchook.bot.commands.ListCommand;
import com.github.pinmacaroon.dchook.bot.commands.ModsCommand;
import com.github.pinmacaroon.dchook.bot.commands.TimeCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SlashCommandInteractionListener extends ListenerAdapter {
    private final Bot BOT;

    public SlashCommandInteractionListener(Bot bot){
        this.BOT = bot;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getGuild() == null) return;
        if (event.getGuild().getIdLong() != this.BOT.getGUILD_ID()) return;
        switch (event.getName()) {
            case "time" -> TimeCommand.run(event);
            case "mods" -> ModsCommand.run(event);
            case "list" -> ListCommand.run(event);
        }
    }
}
