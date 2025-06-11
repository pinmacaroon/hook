package com.github.pinmacaroon.dchook.bot.event;

import com.github.pinmacaroon.dchook.bot.Bot;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReadyEventListener extends ListenerAdapter {
    private final Bot BOT;

    public ReadyEventListener(Bot bot){
        this.BOT = bot;
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Logged in as %s%s!");
    }
}
