package com.github.GrinkieDiscordBot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class EchoCommand implements ICommand {
    @Override
    public String getName(){
        return "echo";
    }

    @Override
    public String getDescription(){
        return "Repeats what you sent.";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event){
        OptionMapping textOption = event.getOption("text"); // Imports the text from discord command
        String textToEcho = "";
        if (textOption != null)
        {
            textToEcho = textOption.getAsString();
        }
        else {
            textToEcho = "You didn't provide any text to echo!";
        }
        event.reply(textToEcho).setEphemeral(false).queue(); // Outputs the text and adds to queue
    }
}
