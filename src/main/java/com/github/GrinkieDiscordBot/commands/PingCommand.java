package com.github.GrinkieDiscordBot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PingCommand implements ICommand {
    @Override
    public String getName(){
        return "ping";
    }

    @Override
    public String getDescription(){
        return "Checks the bot's latency to Discord's gateway.";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event){
        long ping = event.getJDA().getGatewayPing(); // Gets latency to the Discord client
        event.replyFormat("Pong! Gateway ping: %dms",ping).queue();
    }
}
