package com.github.GrinkieDiscordBot.commands.music;

import com.github.GrinkieDiscordBot.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;

/**
 * Makes the bot join the voice channel the user is currently in
 */
public class JoinCommand implements ICommand {
    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getDescription() {
        return "Makes the bot join the voice channel.";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        final Member bot = event.getGuild().getSelfMember(); // Gets the bot instance
        final GuildVoiceState botVoiceState = bot.getVoiceState(); // Gets the voice state of the bot


        if (botVoiceState.inAudioChannel()){ // inAudioChannel can output null if the JDA intents and cache aren't set up correctly.
            event.reply("I'm already in a voice channel!").queue();
            return;
        }

        final Member member = event.getMember(); // Gets the user that used command
        final GuildVoiceState memberVoiceState = member.getVoiceState(); // Gets the voice state of user

        if (!memberVoiceState.inAudioChannel()){ // inAudioChannel can output null if the JDA intents and cache aren't set up correctly.
            event.reply("You are not in a channel!").queue();
            return;
        }


        final AudioManager audioManager = event.getGuild().getAudioManager(); // Gets the audio manager
        final VoiceChannel voiceChannel = memberVoiceState.getChannel().asVoiceChannel(); // Gets the voice channel the user is in

        if(bot.hasPermission(Permission.VOICE_CONNECT)) // Checks if bot has permission to connect to voice
        {
            audioManager.openAudioConnection(voiceChannel); // Opens an audio connection to the channel
            event.replyFormat("Connecting to \uD83D\uDD0A %s", voiceChannel.getName()).queue();
        }
        else {
            event.reply("I do not have voice channel joining permissions!").queue();
        }



    }
}
