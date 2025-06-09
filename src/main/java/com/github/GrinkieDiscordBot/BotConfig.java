package com.github.GrinkieDiscordBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;

/**
 * Handles the setup and configuration of the bot
 * Token - the Discord bot token which is stored in application.properties
 * JdaBuilder - initialises the JDA and adds the commands
 */
@Configuration
public class BotConfig {

    private static final Logger logger = LoggerFactory.getLogger(BotConfig.class);

    @Value("${token}")
    private String token;

    @Bean
    public JDA JdaBuilder()
    {
        try {
            JDA jda = JDABuilder.createDefault(token)
                    .enableIntents(EnumSet.of(
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.GUILD_VOICE_STATES,
                            GatewayIntent.GUILD_PRESENCES,
                            GatewayIntent.MESSAGE_CONTENT
                    )) // Only enable the required intents — allOf can be risky
                    .setMemberCachePolicy(MemberCachePolicy.ALL) // Caches all members
                    .enableCache(CacheFlag.VOICE_STATE) // Caches voice state
                    .addEventListeners(new CommandListener()) // Your command handler
                    .build();

            jda.awaitReady();
            logger.info("Bot ready!");
            try {
                jda.updateCommands()
                        .addCommands(
                                // Adds all the commands to the JDA
                                Commands.slash("ping", "Checks the bot's latency to Discord's gateway."),
                                Commands.slash("info", "Displays information about the bot"),
                                Commands.slash("echo", "Repeats what you sent.")
                                        .addOption(OptionType.STRING, "text", "The text to echo.", true),
                                Commands.slash("roll","Roles dice!")
                                                .addOption(OptionType.INTEGER,"num","The number of sides on the dice."),
                                Commands.slash("palindrome","Is your word a palindrome?")
                                        .addOption(OptionType.STRING,"text", "The word to check."),
                                Commands.slash("join","Makes the bot join a channel."),
                                Commands.slash("leave","Makes the bot leave the voice channel."),
                                Commands.slash("play", "Plays a song\n" + "'/play <Soundcloud link> or <search>'")
                                        .addOption(OptionType.STRING, "url", "Soundcloud URL"),
                                Commands.slash("stop", "Stops the current song."),
                                Commands.slash("skip", "Skips current song."),
                                Commands.slash("np", "Now playing - Shows current song."),
                                Commands.slash("queue", "Shows the current queue."),
                                Commands.slash("repeat", "Repeats current song.")
                        ).queue();
                logger.info("Commands registered!");
            }
            catch (Exception e)
            {
                logger.error("Unable to register commands: ",e);
            }
            return jda;
        }
        catch (Exception e)
        {
            logger.error("Error starting the bot: ",e);
        }
        return null;

    }



}
