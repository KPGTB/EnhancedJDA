package eu.kpgtb.context;

import dev.projectenhanced.enhancedjda.EnhancedBot;
import dev.projectenhanced.enhancedjda.controller.command.EnhancedContext;
import dev.projectenhanced.enhancedjda.controller.command.annotation.Context;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.GenericContextInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;

@Context(type = Command.Type.USER, name = "Get user's avatar")
public class GetAvatarContext extends EnhancedContext<User> {
    public GetAvatarContext(EnhancedBot bot) {
        super(bot);
    }

    @Override
    protected void execute(GenericContextInteractionEvent<User> event) {
        event.reply("Avatar: " + event.getTarget().getEffectiveAvatarUrl()).queue();
    }
}
