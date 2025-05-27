package com.github.GrinkieDiscordBot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.audio.AudioSendHandler;

/**
 * Links the handler, scheduler and player together
 */
public class MusicManager {
    public final AudioPlayer audioPlayer;
    public final TrackScheduler scheduler;
    public final AudioPlayerSendHandler handler;

    public MusicManager(AudioPlayerManager manager) {
        this.audioPlayer= manager.createPlayer(); // Uses the manager to create a player
        this.scheduler = new TrackScheduler(this.audioPlayer); // Assigns the player to the scheduler
        this.audioPlayer.addListener(this.scheduler); // Assigns the scheduler to the player
        this.handler = new AudioPlayerSendHandler(audioPlayer); // Assigns the player to the handler
    }

    public AudioPlayerSendHandler getHandler()
    {
        return handler;
    }
}
