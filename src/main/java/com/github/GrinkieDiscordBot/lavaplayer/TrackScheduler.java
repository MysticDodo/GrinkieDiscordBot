package com.github.GrinkieDiscordBot.lavaplayer;

import com.github.GrinkieDiscordBot.CommandListener;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Deals with the scheduling of tracks, adds them to queue, starts track after other finishes etc.
 */

public class TrackScheduler extends AudioEventAdapter {
    private static final Logger logger = LoggerFactory.getLogger(TrackScheduler.class);

    public final AudioPlayer player;
    public final BlockingQueue<AudioTrack> queue;
    public Boolean repeat =false;

    public TrackScheduler(AudioPlayer player)
    {
        this.player=player;
        this.queue=new LinkedBlockingQueue<>();
    }

    public void addToQueue(AudioTrack track){
        // Because no interrupt is true, if a track is already playing, startTrack will return false
        //which as it's NOT, will change to true, therefore if a track is playing it will add the track
        //the queue instead
        if (!this.player.startTrack(track, true)) {
            logger.info("Track already playing, added to queue");
            this.queue.offer(track);
        } else {
            logger.info("Started playing: " + track.getInfo().title);
        }
    }

    public int queueSize(){
        return queue.size();
    }


    // Starts the next track
    public void nextTrack(){
        this.player.startTrack(this.queue.poll(),false); // Poll adds a new one and removes the last
        // No interrupt is false as using nextTrack to also skip
    }



    // Checks if the next song should play, e.g. doesn't play if music stopped
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason reason){
        if (reason.mayStartNext)
        {
            if (repeat)
            {
                this.player.startTrack(track.makeClone(),false);
                return;
            }
            nextTrack();

        }
    }
}
