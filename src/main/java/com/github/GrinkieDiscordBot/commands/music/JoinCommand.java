package com.github.GrinkieDiscordBot.commands.music;

import com.github.GrinkieDiscordBot.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.DetachedEntityException;
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
        // Ensure the command is only used in a server context
        if (!event.isFromGuild()) {
            event.reply("This command must be used in a server.").setEphemeral(true).queue();
            return;
        }

        final Guild guild = event.getGuild();
        final Member member = event.getMember();

        if (guild == null || member == null) {
            event.reply("Couldn't resolve server or member.").setEphemeral(true).queue();
            return;
        }

        try {
            final Member bot = guild.getSelfMember(); // Gets the bot instance
            final GuildVoiceState botVoiceState = bot.getVoiceState(); // Gets the voice state of the bot
            final GuildVoiceState memberVoiceState = member.getVoiceState(); // Gets the voice state of user

            // If bot is already in a voice channel, do not join another
            if (botVoiceState != null && botVoiceState.inAudioChannel()) {
                event.reply("I'm already in a voice channel!").queue();
                return;
            }

            // If user is not in a voice channel, abort
            if (memberVoiceState == null || !memberVoiceState.inAudioChannel()) {
                event.reply("You are not in a channel!").queue();
                return;
            }

            final AudioManager audioManager = guild.getAudioManager(); // Gets the audio manager
            final VoiceChannel voiceChannel = memberVoiceState.getChannel().asVoiceChannel(); // Gets the voice channel the user is in

            // If the bot has permission to connect to the voice channel, join
            if (bot.hasPermission(voiceChannel, Permission.VOICE_CONNECT)) {
                audioManager.openAudioConnection(voiceChannel); // Opens an audio connection to the channel
                event.replyFormat("Connecting to \uD83D\uDD0A %s", voiceChannel.getName()).queue();
            } else {
                event.reply("I do not have voice channel joining permissions!").queue();
            }
        } catch (DetachedEntityException e) {
            // This can happen if the bot tries to access a server it's not properly cached in
            event.reply("I'm unable to join this server. Please check my permissions and make sure I'm added correctly.").setEphemeral(true).queue();
            e.printStackTrace();
        }
    }
}
