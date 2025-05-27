package com.github.GrinkieDiscordBot.commands.music;

import com.github.GrinkieDiscordBot.commands.ICommand;
import com.github.GrinkieDiscordBot.lavaplayer.MusicManager;
import com.github.GrinkieDiscordBot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class NowPlayingCommand implements ICommand {

    @Override
    public String getName() {
        return "np";
    }

    @Override
    public String getDescription() {
        return "Now playing - Shows current song.";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        final Member bot = event.getGuild().getSelfMember(); // Gets the bot instance
        final GuildVoiceState botVoiceState = bot.getVoiceState(); // Gets the voice state of the bot

        // Checks if bot is in channel
        if (!botVoiceState.inAudioChannel()){ // inAudioChannel can output null if the JDA intents and cache aren't set up correctly.
            event.reply("I'm not in a channel!").queue();
            return;
        }

        final Member member = event.getMember(); // Gets the user that used command
        final GuildVoiceState memberVoiceState = member.getVoiceState(); // Gets the voice state of user

        // Checks if user is in channel
        if (!memberVoiceState.inAudioChannel()){ // inAudioChannel can output null if the JDA intents and cache aren't set up correctly.
            event.reply("You are not in a channel!").queue();
            return;
        }

        // Checks if user in same channel as bot
        if (!memberVoiceState.getChannel().equals(memberVoiceState.getChannel())){
            event.reply("You need to be in the same channel as me for this to work!").queue();
            return;
        }

        // Gets music manager, audio player and track
        final MusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack track = audioPlayer.getPlayingTrack();

        if (track == null){
            event.reply("There is no track playing.").queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder(); // Builds embeded text, a box of information
        embedBuilder.setTitle("Now playing: ");
        embedBuilder.setDescription(track.getInfo().title);
        embedBuilder.setColor(Color.blue);
        embedBuilder.addField("Author", track.getInfo().author, true);
        embedBuilder.setFooter("Songs in queue: " + String.valueOf(musicManager.scheduler.queueSize()));

        event.replyEmbeds(embedBuilder.build()).queue();

    }
}
