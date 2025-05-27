package com.github.GrinkieDiscordBot.commands.music;

import com.github.GrinkieDiscordBot.commands.ICommand;
import com.github.GrinkieDiscordBot.lavaplayer.MusicManager;
import com.github.GrinkieDiscordBot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class RepeatCommand implements ICommand {
    @Override
    public String getName() {
        return "repeat";
    }

    @Override
    public String getDescription() {
        return "Turns on repeat for current song.";
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



        // Swaps the boolean repeat
        boolean newRepeat = !musicManager.scheduler.repeat;
        musicManager.scheduler.repeat = newRepeat;

        event.replyFormat("The player has been set to: **%s**", newRepeat ? "repeating" : "not repeating").queue();




    }
}
