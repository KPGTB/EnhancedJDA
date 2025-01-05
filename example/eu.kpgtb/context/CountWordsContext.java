package eu.kpgtb.context;

import dev.projectenhanced.enhancedjda.EnhancedBot;
import dev.projectenhanced.enhancedjda.controller.command.EnhancedContext;
import dev.projectenhanced.enhancedjda.controller.command.annotation.Context;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.GenericContextInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;

@Context(name = "Count words", type = Command.Type.MESSAGE)
public class CountWordsContext extends EnhancedContext<Message> {
    public CountWordsContext(EnhancedBot bot) {
        super(bot);
    }

    @Override
    protected void execute(GenericContextInteractionEvent<Message> event) {
        event.reply("Words: " + event.getTarget().getContentRaw().split("\\s+").length).queue();
    }
}
