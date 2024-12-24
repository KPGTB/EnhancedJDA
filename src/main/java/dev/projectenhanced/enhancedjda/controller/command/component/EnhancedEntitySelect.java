package dev.projectenhanced.enhancedjda.controller.command.component;

import dev.projectenhanced.enhancedjda.EnhancedBot;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectInteraction;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.function.Consumer;
import java.util.function.Function;

public class EnhancedEntitySelect extends EnhancedComponent<EntitySelectMenu, EntitySelectInteraction> {
    public EnhancedEntitySelect(EnhancedBot bot, Function<String, EntitySelectMenu> configure, Consumer<EntitySelectInteraction> action) {
        super(bot, configure, action);
    }

    public EnhancedEntitySelect(EnhancedBot bot, String permanentId, Function<String, EntitySelectMenu> configure, Consumer<EntitySelectInteraction> action) {
        super(bot, permanentId, configure, action);
    }

    @Override
    public void onEntitySelectInteraction(EntitySelectInteractionEvent event) {
        this.complete(event.getComponentId(),event);
    }
}
