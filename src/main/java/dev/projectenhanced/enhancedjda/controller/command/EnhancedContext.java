package dev.projectenhanced.enhancedjda.controller.command;

import dev.projectenhanced.enhancedjda.EnhancedBot;
import dev.projectenhanced.enhancedjda.controller.command.annotation.CommandPermission;
import dev.projectenhanced.enhancedjda.controller.command.annotation.Context;
import dev.projectenhanced.enhancedjda.controller.command.annotation.GuildCommand;
import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.GenericContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public abstract class EnhancedContext<T> extends ListenerAdapter {
    private final CommandData data;
    private final List<String> guilds;

    public EnhancedContext(EnhancedBot bot) {
        Context contextAnn = getClass().getDeclaredAnnotation(Context.class);
        if(contextAnn == null ) throw new IllegalArgumentException("User Context requires @Context annotation");
        String name = contextAnn.name();
        this.data = Commands.context(contextAnn.type(), name);

        CommandPermission permissionAnn = getClass().getDeclaredAnnotation(CommandPermission.class);
        if(permissionAnn != null && permissionAnn.value().length > 0) {
            this.data.setDefaultPermissions(DefaultMemberPermissions.enabledFor(permissionAnn.value()));
        }

        this.guilds = new ArrayList<>();
        GuildCommand guildCommandAnn = getClass().getDeclaredAnnotation(GuildCommand.class);
        if(guildCommandAnn != null) {
            this.guilds.addAll(Arrays.stream(guildCommandAnn.values()).toList());
        }

        bot.getShardManager().addEventListener(this);
    }

    protected abstract void execute(GenericContextInteractionEvent<T> event);

    @Override
    @SuppressWarnings("unchecked")
    public void onGenericContextInteraction(GenericContextInteractionEvent<?> event) {
        if(event.getName().equalsIgnoreCase(this.data.getName())) {
            execute((GenericContextInteractionEvent<T>) event);
        }
    }
}
