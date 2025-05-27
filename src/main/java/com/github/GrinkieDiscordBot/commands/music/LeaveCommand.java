package com.github.GrinkieDiscordBot.commands.music;

import com.github.GrinkieDiscordBot.commands.ICommand;
import com.github.GrinkieDiscordBot.lavaplayer.MusicManager;
import com.github.GrinkieDiscordBot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

/**
 * Makes the bot leave the current voice channel.
 */
public class LeaveCommand implements ICommand {

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getDescription() {
        return "Makes the bot leave the current voice channel";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        final Member bot = event.getGuild().getSelfMember(); // Gets the bot instance
        final GuildVoiceState botVoiceState = bot.getVoiceState(); // Gets the voice state of the bot

        if (!botVoiceState.inAudioChannel()){ // inAudioChannel can output null if the JDA intents and cache aren't set up correctly.
            event.reply("I'm not in a voice channel!").queue();
            return;
        }

        final AudioManager audioManager = event.getGuild().getAudioManager(); // Gets the audio manager
        final MusicManager musicManager= PlayerManager.getInstance().getMusicManager(event.getGuild()); // Gets the music manager

        // Resets everything
        musicManager.scheduler.repeat=false;
        musicManager.scheduler.queue.clear();
        musicManager.audioPlayer.stopTrack();

        event.reply("Leaving channel!").queue();
        audioManager.closeAudioConnection(); // Closes the connection, leaves
    }
}
