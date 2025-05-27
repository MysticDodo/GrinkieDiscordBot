package com.github.GrinkieDiscordBot;

import com.github.GrinkieDiscordBot.commands.*;
import com.github.GrinkieDiscordBot.commands.music.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * The event listener, handles the commands
 * Stores the commands in a map, does some validation and executes the command.
 */
@Component
public class CommandListener extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(CommandListener.class);

    private final Map<String, ICommand> commands = new HashMap<>();


    public CommandListener(){
        commands.put("ping", new PingCommand());
        commands.put("info", new InfoCommand());
        commands.put("echo", new EchoCommand());
        commands.put("roll", new RollCommand());
        commands.put("palindrome", new PalindromeCommand());
        commands.put("join", new JoinCommand());
        commands.put("leave", new LeaveCommand());
        commands.put("play", new PlayCommand());
        commands.put("stop", new StopCommand());
        commands.put("skip", new SkipCommand());
        commands.put("np", new NowPlayingCommand());
        commands.put("queue", new QueueCommand());
        commands.put("repeat", new RepeatCommand());
        logger.info("Registered {} commands", commands.size());

    }

    @Override
    public void onReady(@NotNull ReadyEvent event){
        logger.info("JDA is ready! Logged in as {}#{}",
                event.getJDA().getSelfUser().getName(),
                event.getJDA().getSelfUser().getDiscriminator());
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event){
        //Checks if the command comes from a bot
        if(event.getUser().isBot()) return;
        //Makes sure only responds to events in guild.
        if(!event.isFromGuild()) return;
        String commandName = event.getName();
        ICommand command= commands.get(commandName);
        if (command != null){
            logger.debug("Executing slash command: {} from user: {}",
                    commandName, event.getJDA().getSelfUser().getName());
            command.execute(event);
        }
        else{
            logger.warn("Unknown slash command: {} from user: {}",
                    commandName, event.getJDA().getSelfUser().getName());
            event.reply("Unknown command!").setEphemeral(true).queue();
        }

    }

}
