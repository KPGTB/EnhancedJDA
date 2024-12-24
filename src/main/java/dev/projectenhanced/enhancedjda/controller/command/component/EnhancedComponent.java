package dev.projectenhanced.enhancedjda.controller.command.component;

import dev.projectenhanced.enhancedjda.EnhancedBot;
import lombok.Getter;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ItemComponent;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class EnhancedComponent<T,Z> extends ListenerAdapter {
    protected final String componentId;
    @Getter private final T component;
    protected Consumer<Z> action;

    public EnhancedComponent(EnhancedBot bot, String permanentId, Function<String,T> configure, Consumer<Z> action) {
        this.componentId = permanentId;

        this.component = configure.apply(this.componentId);
        this.action = action;

        bot.getShardManager().addEventListener(this);
    }

    public EnhancedComponent(EnhancedBot bot, Function<String,T> configure, Consumer<Z> action) {
        this(bot,"enhanced_component_" + System.currentTimeMillis() + "_" + Math.random(), configure,action);
    }

    protected void complete(String componentId, Z data) {
        if(this.componentId.equalsIgnoreCase(componentId)) {
            if(this.action != null) this.action.accept(data);
        }
    }
}
