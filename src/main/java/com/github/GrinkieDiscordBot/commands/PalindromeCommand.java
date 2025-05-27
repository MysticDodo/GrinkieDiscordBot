package com.github.GrinkieDiscordBot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class PalindromeCommand implements ICommand {
    @Override
    public String getName()
    {
        return "palindrome";

    }

    @Override
    public String getDescription(){
        return "Is your word a palindrome?";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping textOption = event.getOption("text"); // Imports text from discord command
        boolean output = true;
        if (textOption != null)
        {
            String input = textOption.getAsString();
            input = input.replace(" ", "");
            for (int x = 0; x < input.length() / 2; x++) {
                if (input.charAt(x) != input.charAt((input.length() - 1) - x)) {
                    output = false;
                    break;
                }
            }
            if (output){
                event.reply(input +" is a palindrome!").queue();
            }
            else{
                event.reply(input +" is not a palindrome!").queue();
            }
        }
        else {
            event.reply("You didn't provide any text to check!").queue();
        }
    }

}
