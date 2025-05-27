package com.github.GrinkieDiscordBot.commands.music;

import com.github.GrinkieDiscordBot.commands.ICommand;
import com.github.GrinkieDiscordBot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import static org.springframework.util.ResourceUtils.isUrl;

public class PlayCommand implements ICommand {
    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Plays a song\n" +
                "'/play <Soundcloud link> or <search>'";
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        final Member bot = event.getGuild().getSelfMember(); // Gets the bot instance
        final GuildVoiceState botVoiceState = bot.getVoiceState(); // Gets the voice state of the bot
        OptionMapping stringOption = event.getOption("url");
        if(stringOption == null){
            event.reply("Correct usage is: '/play <link>'").queue();
            return;
        }

        if (!botVoiceState.inAudioChannel()){ // inAudioChannel can output null if the JDA intents and cache aren't set up correctly.
            new JoinCommand().execute(event);
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


        String url = stringOption.getAsString();

        // If its not a URL, it searches soundcloud.
        if (!isUrl(url))
        {
            url = "scsearch:"+url;
        }

        event.deferReply().queue();

        PlayerManager.getInstance()
                .loadAndPlay(event.getChannel().asTextChannel(),url);

        event.getHook().sendMessage("Track queued!").queue();


    }
}
