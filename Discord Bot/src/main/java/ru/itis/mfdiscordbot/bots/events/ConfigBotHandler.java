package ru.itis.mfdiscordbot.bots.events;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import ru.itis.mfdiscordbot.bots.DiscordBot;
import ru.itis.mfdiscordbot.config.BotConfig;
import ru.itis.mfdiscordbot.utils.ConfigLoader;
import ru.itis.mfdiscordbot.utils.PropertiesLoader;

@Slf4j
public class ConfigBotHandler extends ListenerAdapter {

    protected List<String> configCommands;
    protected DiscordBot bot;

    public ConfigBotHandler(DiscordBot bot){
        configCommands = Arrays.asList(PropertiesLoader.getInstance().getProperty("discord.bot.config-commands").split(", "));
        this.bot = bot;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (configCommands.contains(event.getMessage().getContentRaw().toLowerCase()) && bot.isActive()) {
            BotConfig[] configs = ConfigLoader.getBotConfigs(event.getMessage().getAttachments().get(0));
            if (checkConfig(configs)) {
                bot.connect(configs);
            } else {
                event.getChannel().sendMessage("Неверный или пустой конифг файл, напишите /help чтобы узнать подходящий формат").queue();
            }
        }
    }

    protected boolean checkConfig(BotConfig[] config) {
        return config.length != 0;
    }
}
