package eu.kpgtb.listener;

import dev.projectenhanced.enhancedjda.logger.EnhancedLogger;
import eu.kpgtb.data.UserMessage;
import dev.projectenhanced.enhancedjda.EnhancedBot;
import dev.projectenhanced.enhancedjda.controller.listener.EnhancedListener;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.SQLException;
import java.util.Date;

public class ExampleListener extends EnhancedListener {
    private final EnhancedBot bot;

    public ExampleListener(EnhancedBot bot) {
        super(bot);
        this.bot = bot;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        EnhancedLogger.getLogger().info("User {} sent a message: {}", event.getAuthor().getAsTag(), event.getMessage().getContentRaw());
        UserMessage message = new UserMessage(null,
                event.getAuthor().getAsTag(),
                event.getMessage().getContentRaw(),
                new Date(event.getMessage().getTimeCreated().toInstant().toEpochMilli()),
                event.getGuild());
        try {
            bot.getDataController().getDao(UserMessage.class, Integer.class).create(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
