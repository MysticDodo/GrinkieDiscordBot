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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueueCommand implements ICommand {

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getDescription() {
        return "Shows the songs in the queue.";
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

        // Gets music manager, audio player and track
        final MusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        final BlockingQueue<AudioTrack> queue =  musicManager.scheduler.queue;

        // Checks if queue is empty
        if (queue.isEmpty())
        {
            event.reply("There is nothing in the queue!").queue();
            return;
        }

        final int trackCount = Math.min(queue.size(), 15);
        final List<AudioTrack> tracks = new ArrayList<>(queue);

        EmbedBuilder embedBuilder = new EmbedBuilder();// Builds embeded text, a box of information
        embedBuilder.setTitle("Current queue: ");
        embedBuilder.setColor(Color.green);

        // Loops through first n tracks and adds them to the embed builder
        for (int i=0; i<trackCount; i++){
            final AudioTrack track = tracks.get(i);
            embedBuilder.addField(track.getInfo().title, "By: "+track.getInfo().author + " ["+formatTime(track.getDuration()) + "]",false);
        }

        // If theres more than 15, it shows the number of remaining
        if (tracks.size()> trackCount)
        {
            embedBuilder.setFooter("+" + String.valueOf(tracks.size()-trackCount) + " more...");
        }


        event.replyEmbeds(embedBuilder.build()).queue();

    }
    // Used to turn the time in ms from the song in h:m:s
    private String formatTime(long timeInMs){
        final long hours = timeInMs / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMs / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMs % TimeUnit.MINUTES.toMillis(1) / TimeUnit. SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);

    }
}

