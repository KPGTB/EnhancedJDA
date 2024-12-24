package dev.projectenhanced.enhancedjda.controller.command.component;

import dev.projectenhanced.enhancedjda.EnhancedBot;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.function.Consumer;
import java.util.function.Function;

public class EnhancedStringSelect extends EnhancedComponent<StringSelectMenu, StringSelectInteractionEvent> {
    public EnhancedStringSelect(EnhancedBot bot, Function<String, StringSelectMenu> configure, Consumer<StringSelectInteractionEvent> action) {
        super(bot, configure, action);
    }

    public EnhancedStringSelect(EnhancedBot bot, String permanentId, Function<String, StringSelectMenu> configure, Consumer<StringSelectInteractionEvent> action) {
        super(bot, permanentId, configure, action);
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        this.complete(event.getComponentId(),event);
    }
}
