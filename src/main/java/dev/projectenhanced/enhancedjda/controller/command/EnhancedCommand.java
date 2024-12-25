package dev.projectenhanced.enhancedjda.controller.command;

import dev.projectenhanced.enhancedjda.EnhancedBot;
import dev.projectenhanced.enhancedjda.controller.command.annotation.*;
import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Getter
public abstract class EnhancedCommand extends ListenerAdapter {

    private final SlashCommandData commandData;
    private final List<String> guilds;

    private final Map<String, Map<String, Method>> methods;
    private final Map<String,Object> invokers;

    public EnhancedCommand(EnhancedBot bot) {
        this.methods = new HashMap<>();
        this.invokers = new HashMap<>();

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

        if(getClass().isAnnotationPresent(NsfwCommand.class)) this.commandData.setNSFW(true);
        if(getClass().isAnnotationPresent(GuildOnlyCommand.class)) this.commandData.setGuildOnly(true);

        try {
            scanClass(this.getClass(), this,null);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        bot.getShardManager().addEventListener(this);
    }

    private void scanClass(Class<?> clazz, Object invoker,SubcommandGroupData groupData) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String groupName = groupData != null ? groupData.getName() : "";
        this.methods.putIfAbsent(groupName, new HashMap<>());
        this.invokers.put(groupName,invoker);

        for(Method method : clazz.getDeclaredMethods()) {
            if(method.isSynthetic()) {
                continue;
            }
            if(method.getDeclaredAnnotation(Utility.class) != null) {
                continue;
            }
            if(method.getParameterCount() != 1 || !method.getParameters()[0].getType().isAssignableFrom(SlashCommandInteractionEvent.class)) {
                continue;
            }
            String name = method.getName().toLowerCase();
            boolean isMain = method.isAnnotationPresent(MainCommand.class) && groupData == null;

            CommandDescription descriptionAnn = method.getDeclaredAnnotation(CommandDescription.class);
            String description = descriptionAnn != null ? descriptionAnn.value() : "Subcommand created using EnhancedJDA";

            CommandOption[] options = method.getDeclaredAnnotationsByType(CommandOption.class);

            if(isMain) {
                this.methods.get(groupName).put("", method);
                for (CommandOption option : options) {
                    commandData.addOption(option.type(),option.name(),option.description(),option.required(),option.autoComplete());
                }
            } else {
                this.methods.get(groupName).put(name,method);
                SubcommandData subcommandData = new SubcommandData(name,description);
                for (CommandOption option : options) {
                    subcommandData.addOption(option.type(),option.name(),option.description(),option.required(),option.autoComplete());
                }

                if(groupData == null) {
                    commandData.addSubcommands(subcommandData);
                } else {
                    groupData.addSubcommands(subcommandData);
                }
            }
        }

        if(groupData != null) {
            commandData.addSubcommandGroups(groupData);
        } else {
            for (Class<?> c : clazz.getDeclaredClasses()) {
                CommandDescription descriptionAnn = c.getDeclaredAnnotation(CommandDescription.class);
                String description = descriptionAnn != null ? descriptionAnn.value() : "Subcommand group created using EnhancedJDA";

                scanClass(
                        c,
                        c.getDeclaredConstructor(clazz).newInstance(invoker),
                        new SubcommandGroupData(c.getSimpleName().toLowerCase(), description)
                );
            }
        }
    }

    protected void autoComplete(CommandAutoCompleteInteractionEvent event, String option, String value) {}

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equalsIgnoreCase(this.commandData.getName())) {
            String group = event.getSubcommandGroup();
            String subcommand = event.getSubcommandName();
            if(group == null) group = "";
            if(subcommand == null) subcommand = "";

            Object invoker = this.invokers.get(group);
            if(invoker == null) return;

            if(!this.methods.containsKey(group)) return;
            if(!this.methods.get(group).containsKey(subcommand)) return;

            try {
                this.methods.get(group).get(subcommand).invoke(invoker,event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        if(event.getName().equalsIgnoreCase(this.commandData.getName())) {
            this.autoComplete(event, event.getFocusedOption().getName(), event.getFocusedOption().getValue());
        }
    }
}
