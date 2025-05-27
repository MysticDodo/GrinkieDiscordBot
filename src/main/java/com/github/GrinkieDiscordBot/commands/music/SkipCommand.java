package com.github.GrinkieDiscordBot.commands.music;

import com.github.GrinkieDiscordBot.commands.ICommand;
import com.github.GrinkieDiscordBot.lavaplayer.MusicManager;
import com.github.GrinkieDiscordBot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class SkipCommand implements ICommand {


    @Override
    public String getName() {
        return "skips";
    }

    @Override
    public String getDescription() {
        return "Skips the current song";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        final Member bot = event.getGuild().getSelfMember(); // Gets the bot instance
        final GuildVoiceState botVoiceState = bot.getVoiceState(); // Gets the voice state of the bot


        if (!botVoiceState.inAudioChannel()){ // inAudioChannel can output null if the JDA intents and cache aren't set up correctly.
            event.reply("I'm not in a channel!").queue();
            return;
        }

        final Member member = event.getMember(); // Gets the user that used command
        final GuildVoiceState memberVoiceState = member.getVoiceState(); // Gets the voice state of user

        if (!memberVoiceState.inAudioChannel()){ // inAudioChannel can output null if the JDA intents and cache aren't set up correctly.
            event.reply("You are not in a channel!").queue();
            return;
        }

        // Checks if user in same channel as bot
        if (!memberVoiceState.getChannel().equals(memberVoiceState.getChannel())){
            event.reply("You need to be in the same channel as me for this to work!").queue();
            return;
        }

        // Gets the manager
        final MusicManager musicManager= PlayerManager.getInstance().getMusicManager(event.getGuild());

        musicManager.scheduler.nextTrack(); // Skips track

        AudioTrack track = musicManager.audioPlayer.getPlayingTrack();

        EmbedBuilder embedBuilder = new EmbedBuilder(); // Builds embeded text, a box of information
        embedBuilder.setTitle("Track skipped! - Now playing: ");
        embedBuilder.setDescription(track.getInfo().title);
        embedBuilder.setColor(Color.blue);
        embedBuilder.addField("Author", track.getInfo().author, true);
        embedBuilder.setFooter("Songs in queue: " + String.valueOf(musicManager.scheduler.queueSize()));

        event.replyEmbeds(embedBuilder.build()).queue();

    }
}
