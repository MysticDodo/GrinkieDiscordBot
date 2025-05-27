package com.github.GrinkieDiscordBot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * Handles the audio being sent, checks if can be sent
 */
public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private final ByteBuffer buffer; // Used to store audio in memory
    private final MutableAudioFrame frame; //

    public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.buffer= ByteBuffer.allocate(1024); // How many bytes to allocate to the buffer
        this.frame = new MutableAudioFrame();
        this.frame.setBuffer(buffer); // Everything written to the frame will be written to the buffer
    }

    // Opus is an audio codec, lavaplayer has it enabled by default.
    @Override
    public boolean isOpus() {
        return true;
    }

    @Override
    public boolean canProvide() { // Checks if audio should be sent
        buffer.clear(); // 🛠 clear before providing
        boolean result = this.audioPlayer.provide(this.frame);

        // ✅ Step 4: Debug Logs
        System.out.println("[AudioSendHandler] canProvide: " + result);
        if (result && audioPlayer.getPlayingTrack() != null) {
            System.out.println("[AudioSendHandler] Playing: " + audioPlayer.getPlayingTrack().getInfo().title);
        } else {
            System.out.println("[AudioSendHandler] No track playing.");
        }

        return result;
    }


    @Override
    public ByteBuffer provide20MsAudio() { // Returns 20ms of audio
        buffer.flip();
        return buffer; // Flips the buffer and starts at 0 so JDA can read from the start
    }
}
