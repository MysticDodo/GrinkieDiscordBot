package com.github.GrinkieDiscordBot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
/**
 * The interface for the commands.
 * getName - returns the name of the command
 * getDescription - returns the description of the command
 * execute - allows execution of the command
 */
public interface ICommand {
    String getName();

    String getDescription();

    void execute(SlashCommandInteractionEvent event);
}
