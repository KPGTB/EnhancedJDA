package dev.projectenhanced.enhancedjda.controller.command;

import dev.projectenhanced.enhancedjda.EnhancedBot;
import dev.projectenhanced.enhancedjda.controller.command.annotation.CommandDescription;
import dev.projectenhanced.enhancedjda.controller.command.annotation.CommandPermission;
import dev.projectenhanced.enhancedjda.controller.command.annotation.GuildCommand;
import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public abstract class EnhancedCommand extends ListenerAdapter {

    private final SlashCommandData commandData;
    private final List<String> guilds;

    public EnhancedCommand(EnhancedBot bot) {
        String command = getClass().getSimpleName().toLowerCase().replace("command", "");
        String description = "Command created using EnhancedJDA framework";
        CommandDescription descriptionAnn = getClass().getDeclaredAnnotation(CommandDescription.class);
        if(descriptionAnn != null) description = descriptionAnn.value();

        this.guilds = new ArrayList<>();
        GuildCommand guildCommandAnn = getClass().getDeclaredAnnotation(GuildCommand.class);
        if(guildCommandAnn != null) {
            this.guilds.addAll(Arrays.stream(guildCommandAnn.values()).toList());
        }

        this.commandData = Commands.slash(command,description);
        CommandPermission permissionAnn = getClass().getDeclaredAnnotation(CommandPermission.class);
        if(permissionAnn != null && permissionAnn.value().length > 0) {
            this.commandData.setDefaultPermissions(DefaultMemberPermissions.enabledFor(permissionAnn.value()));
        }
        configure(this.commandData);

        bot.getShardManager().addEventListener(this);
    }

    protected abstract void configure(SlashCommandData data);
    protected abstract void execute(SlashCommandInteractionEvent event);
    protected void autoComplete(CommandAutoCompleteInteractionEvent event, String option, String value) {}

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equalsIgnoreCase(this.commandData.getName())) {
            this.execute(event);
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        if(event.getName().equalsIgnoreCase(this.commandData.getName())) {
            this.autoComplete(event, event.getFocusedOption().getName(), event.getFocusedOption().getValue());
        }
    }
}
