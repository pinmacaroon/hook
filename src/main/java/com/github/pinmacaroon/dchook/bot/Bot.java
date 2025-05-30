package com.github.pinmacaroon.dchook.bot;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;

public class Bot {
    private final DiscordClient CLIENT;
    private GatewayDiscordClient GATEWAY_CLIENT;
    private long GUILD_ID;
    private long CHANNEL_ID;
    private final char PREFIX;

    public Bot(String token, char prefix){
        this.CLIENT = DiscordClient.create(token);
        this.PREFIX = prefix;
    }

    public Bot(String token){
        this.CLIENT = DiscordClient.create(token);
        this.PREFIX = '$';
    }

    public DiscordClient getCLIENT() {
        return CLIENT;
    }

    @Nullable
    public Thread start(){
        //TODO fix possible race condition so that the bot doesn't start with nonexistent ids
        /*
        if(GUILD_ID == 0){
            return null;
        }
        if(CHANNEL_ID == 0){
            return null;
        }
        */
        Thread lifecyclethread = new Thread(() -> {
            Mono<Void> login = this.CLIENT.withGateway(
                    (GatewayDiscordClient gateway) -> {
                        this.GATEWAY_CLIENT = gateway;
                        System.out.println("gateway login");
                        Mono<Void> ready = gateway.on(ReadyEvent.class, readyEvent ->
                            Mono.fromRunnable(() -> {
                                final User user = readyEvent.getSelf();
                                //noinspection deprecation
                                System.out.println(MessageFormat.format(
                                        "logged bot in as {0}#{1}",
                                        user.getUsername(),
                                        user.getDiscriminator()
                                ));
                            })
                        ).then();

                        Mono<Void> message = gateway.on(MessageCreateEvent.class, messageCreateEvent ->
                            Mono.fromRunnable(() -> Messenger.handeMessage(messageCreateEvent, this))
                        ).then();
                        return ready.and(message);
                    }
            );
            login.block();
        });
        lifecyclethread.start();
        return lifecyclethread;
    }

    public void stop(){
        GATEWAY_CLIENT.logout().block();
    }

    public GatewayDiscordClient getGATEWAY_CLIENT() {
        return GATEWAY_CLIENT;
    }

    public long getGUILD_ID() {
        return GUILD_ID;
    }

    public void setGUILD_ID(long GUILD_ID) {
        this.GUILD_ID = GUILD_ID;
    }

    public long getCHANNEL_ID() {
        return CHANNEL_ID;
    }

    public void setCHANNEL_ID(long CHANNEL_ID) {
        this.CHANNEL_ID = CHANNEL_ID;
    }

    public char getPREFIX() {
        return PREFIX;
    }
}
