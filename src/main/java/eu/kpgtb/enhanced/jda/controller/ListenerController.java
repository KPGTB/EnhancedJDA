package eu.kpgtb.enhanced.jda.controller;

import eu.kpgtb.enhanced.jda.EnhancedBot;
import eu.kpgtb.enhanced.jda.util.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class ListenerController {
    private final EnhancedBot bot;

    public ListenerController(EnhancedBot bot) {
        this.bot = bot;
    }

    public void registerListeners(String packageName) {
        var ref = new Object() {
            int registered = 0;
            int found = 0;
            int errors = 0;
        };

        bot.getShardManager().addEventListener(
                ReflectionUtil.getAllClassesInPackage(bot.getClass(), packageName,EnhancedListener.class)
                        .stream().map(clazz -> {
                            ref.found++;
                            try {
                                EnhancedListener listener = (EnhancedListener) clazz.getDeclaredConstructor(EnhancedBot.class).newInstance(this.bot);
                                ref.registered++;
                                return listener;
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                     NoSuchMethodException e) {
                                ref.errors++;
                                this.bot.getLogger().error("Error when registering listener from {}", clazz.getName());
                                e.printStackTrace();
                                return null;
                            }
                        }).filter(Objects::nonNull).toArray()
        );

        this.bot.getLogger().info("Registered {}/{} listeners from {}. Errors: {}", ref.registered,ref.found,packageName,ref.errors);
    }
}
