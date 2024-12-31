package dev.projectenhanced.enhancedjda.controller.listener;

import dev.projectenhanced.enhancedjda.EnhancedBot;
import dev.projectenhanced.enhancedjda.logger.EnhancedLogger;
import dev.projectenhanced.enhancedjda.util.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class ListenerController {
    private final EnhancedBot bot;

    public ListenerController(EnhancedBot bot) {
        this.bot = bot;
    }

    /**
     * Register all listeners in certain package
     * @param packageName Name of package
     */
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
                                EnhancedLogger.getLogger().error("Error when registering listener from {}", clazz.getName());
                                e.printStackTrace();
                                return null;
                            }
                        }).filter(Objects::nonNull).toArray()
        );

        EnhancedLogger.getLogger().info("Registered {}/{} listeners from {}. Errors: {}", ref.registered,ref.found,packageName,ref.errors);
    }
}
