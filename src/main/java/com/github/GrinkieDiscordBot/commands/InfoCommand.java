package com.github.GrinkieDiscordBot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class InfoCommand implements ICommand {
    @Override
    public String getName(){
        return "info";
    }

    @Override
    public String getDescription(){
        return "Displays information about the bot.";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event){
        EmbedBuilder embedBuilder = new EmbedBuilder(); // Builds embeded text, a box of information
        embedBuilder.setTitle("Bot information");
        embedBuilder.setDescription("This the Grinkie Discord bot I made so I could learn, " +
                "the name Grinkie comes from a plushie I bought for my girlfriend!");
        embedBuilder.setColor(Color.pink);
        embedBuilder.addField("Author","MysticDodo",true);
        embedBuilder.addField("Langauge","Java - Spring Boot",true);
        embedBuilder.addField("Library","JDA (Java Discord API)",true);
        embedBuilder.setFooter(":)");

        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
