package com.github.pinmacaroon.dchook.bot;

import com.github.pinmacaroon.dchook.Hook;
import com.github.pinmacaroon.dchook.conf.ModConfigs;
import discord4j.core.event.domain.message.MessageCreateEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class Messenger {
    public static final Pattern MENTION_PATTERN = Pattern.compile("<([@#])(\\d+)>");
    public static void handeMessage(MessageCreateEvent messageCreateEvent, Bot bot){
        if(
                (messageCreateEvent.getMessage().getChannelId().asLong() == bot.getCHANNEL_ID())
                        && Hook.MINECRAFT_SERVER != null){
            if(messageCreateEvent.getMessage().getAuthorAsMember().block()==null){
                return;
            }

            if(
                    messageCreateEvent.getMessage().getAuthor().isPresent()
                            && messageCreateEvent.getMessage().getAuthor().get().isBot()){
                return;
            }

            String raw_message = messageCreateEvent.getMessage().getContent();
            if(raw_message.endsWith("//") && ModConfigs.FUNCTIONS_ALLOWOOCMESSAGES){
                return;
            }
            //TODO make mentions
            /*Matcher matcher = MENTION_PATTERN.matcher(raw_message);
            while (matcher.find()){
                raw_message.
            }*/

            MutableText signature = Text.literal(
                    "<@"+messageCreateEvent.getMessage().getAuthorAsMember().block().getUsername()+"> "
            );
            MutableText content = Text.literal(
                    (raw_message.isBlank())
                            ? "<image>" : raw_message
            );
            MutableText formatted = signature.formatted(Formatting.BLUE)
                            .append(content);
            Hook.MINECRAFT_SERVER.getPlayerManager().broadcast(formatted, false);
        }
    }
}
