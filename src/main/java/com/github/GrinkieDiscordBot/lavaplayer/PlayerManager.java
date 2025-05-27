package com.github.GrinkieDiscordBot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Uses the music manager to create a player instance
 */
public class PlayerManager {
    private static PlayerManager Instance;
    private final AudioPlayerManager audioPlayerManager;
    private final Map<Long, MusicManager> musicManagers; // Links the guild ID to the music manager

    public PlayerManager(){
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public MusicManager getMusicManager(Guild guild)
    {
        // Returns the music manager, if absent it uses guildId to create a new
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildID)-> {
            // Intitialises a musicManager with the audioPlayerManager
            final MusicManager musicManager = new MusicManager(this.audioPlayerManager);
            // Uses the musicManager to set the handler to guild
            guild.getAudioManager().setSendingHandler(musicManager.getHandler());

            return musicManager;
        });
    }

    // Loads and plays the track
    public void loadAndPlay(TextChannel channel, String trackUrl){
        // Uses the channel to get guild, to get music manager
        final MusicManager musicManager = this.getMusicManager(channel.getGuild());

        // Loads item to the manager, needs a result handler to handle when track loaded
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {

            // When track loaded add to queue
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.addToQueue(track);
                EmbedBuilder embedBuilder = new EmbedBuilder(); // Builds embeded text, a box of information
                embedBuilder.setTitle("Adding to queue: ");
                embedBuilder.setDescription(track.getInfo().title);
                embedBuilder.setColor(Color.red);
                embedBuilder.addField("Author", track.getInfo().author, true);
                embedBuilder.setFooter("Songs in queue: " + String.valueOf(musicManager.scheduler.queueSize()));


                channel.sendMessageEmbeds(embedBuilder.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();

                for (final AudioTrack track : tracks){
                    musicManager.scheduler.addToQueue(track);
                }

                EmbedBuilder embedBuilder = new EmbedBuilder(); // Builds embeded text, a box of information
                embedBuilder.setTitle("Adding to queue: ");
                embedBuilder.setDescription(String.valueOf(tracks.size())+" tracks from playlist.");
                embedBuilder.setColor(Color.orange);
                embedBuilder.addField("Playlist", audioPlaylist.getName(), true);
                embedBuilder.setFooter("Songs in queue: " + String.valueOf(musicManager.scheduler.queueSize()));

                // Adds all the tracks to queue.
                channel.sendMessageEmbeds(embedBuilder.build()).queue();

            }

            @Override
            public void noMatches() {
                channel.sendMessage("Can't find song!").queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                channel.sendMessage("Loading failed!").queue();
            }
        });
    }


    // Returns the player manager instance
    public static PlayerManager getInstance()
    {
        if (Instance==null)
        {
            Instance = new PlayerManager();
        }
        return Instance;
    }
}
