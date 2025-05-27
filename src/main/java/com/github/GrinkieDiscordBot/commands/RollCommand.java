package com.github.GrinkieDiscordBot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Random;

public class RollCommand implements ICommand {
    @Override
    public String getName(){
        return "roll";
    }

    @Override
    public String getDescription(){
        return "Rolls a dice!";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event){
        Random random = new Random();
        OptionMapping numOption = event.getOption("num"); // Imports the number from Discord command
        //Discord automatically handles input of anything other than int
        if (numOption!= null){
            int num = numOption.getAsInt();
            if (num<1){
                event.reply("The number has to be above 0!").queue();
            }
            else {
                int ranNum = random.nextInt(num) + 1;
                event.reply(String.valueOf(ranNum)).queue();
            }
        }
        else {
            event.reply("You didn't input how many sides of the dice you wanted!").queue();
        }

    }
}
