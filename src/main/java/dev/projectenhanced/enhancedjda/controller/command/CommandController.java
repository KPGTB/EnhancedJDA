package dev.projectenhanced.enhancedjda.controller.command;

import dev.projectenhanced.enhancedjda.EnhancedBot;
import dev.projectenhanced.enhancedjda.logger.EnhancedLogger;
import dev.projectenhanced.enhancedjda.util.ReflectionUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CommandController extends ListenerAdapter {
    private final EnhancedBot bot;
    private final boolean production;

    private final List<CommandData> globalCommands;
    private final Map<CommandData, List<String>> guildCommands;

    public CommandController(EnhancedBot bot) {
        this.bot = bot;
        this.production = bot.getDotenv().get("ENV").equalsIgnoreCase("production");
        this.globalCommands = new ArrayList<>();
        this.guildCommands = new HashMap<>();

        bot.getShardManager().addEventListener(this);
    }

    /**
     * Register all commands from specified package
     * @param packageName Name of that package
     */
    public void registerCommands(String packageName) {
        var ref = new Object() {
            int registered = 0;
            int found = 0;
            int errors = 0;
        };

        ReflectionUtil.getAllClassesInPackage(bot.getClass(), packageName, EnhancedCommand.class)
                .forEach(clazz -> {
                    ref.found++;
                    try {
                        EnhancedCommand command = (EnhancedCommand) clazz.getDeclaredConstructor(EnhancedBot.class).newInstance(this.bot);
                        ref.registered++;

                        if(command.getGuilds().isEmpty()) {
                            this.globalCommands.add(command.getCommandData());
                        } else {
                            this.guildCommands.put(command.getCommandData(), command.getGuilds());
                        }
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        ref.errors++;
                        EnhancedLogger.getLogger().error("Error when registering command from {}", clazz.getName());
                        e.printStackTrace();
                    }
                });

        EnhancedLogger.getLogger().info("Registered {}/{} commands from {}. Errors: {}", ref.registered,ref.found,packageName,ref.errors);
    }

    /**
     * Register all contexts from specified package
     * @param packageName Name of that package
     */
    public void registerContexts(String packageName) {
        var ref = new Object() {
            int registered = 0;
            int found = 0;
            int errors = 0;
        };

        ReflectionUtil.getAllClassesInPackage(bot.getClass(), packageName, EnhancedContext.class)
                .forEach(clazz -> {
                    ref.found++;
                    try {
                        EnhancedContext context = (EnhancedContext) clazz.getDeclaredConstructor(EnhancedBot.class).newInstance(this.bot);
                        ref.registered++;

                        if(context.getGuilds().isEmpty()) {
                            this.globalCommands.add(context.getData());
                        } else {
                            this.guildCommands.put(context.getData(), context.getGuilds());
                        }
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        ref.errors++;
                        EnhancedLogger.getLogger().error("Error when registering context from {}", clazz.getName());
                        e.printStackTrace();
                    }
                });

        EnhancedLogger.getLogger().info("Registered {}/{} contexts from {}. Errors: {}", ref.registered,ref.found,packageName,ref.errors);
    }

    @Override
    public void onReady(ReadyEvent event) {
        if(this.production) {
            event.getJDA().updateCommands().addCommands(this.globalCommands).queue();
        }

        event.getJDA().getGuilds().forEach(this::addGuildCommands);
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        addGuildCommands(event.getGuild());
    }

    private void addGuildCommands(Guild guild) {
        List<CommandData> commands = new ArrayList<>();

        if(!this.production) {
            commands.addAll(this.globalCommands);
        }
        this.guildCommands.forEach((data,guilds) -> {
            if(guilds.contains(guild.getId())) {
                commands.add(data);
            }
        });

        guild.updateCommands().addCommands(commands).queue();
    }
}
