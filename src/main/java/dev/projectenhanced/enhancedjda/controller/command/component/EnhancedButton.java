package dev.projectenhanced.enhancedjda.controller.command.component;

import dev.projectenhanced.enhancedjda.EnhancedBot;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.function.Consumer;
import java.util.function.Function;

public class EnhancedButton extends EnhancedComponent<Button, ButtonInteractionEvent>{
    public EnhancedButton(EnhancedBot bot, Function<String, Button> configure, Consumer<ButtonInteractionEvent> action) {
        super(bot, configure, action);
    }

    public EnhancedButton(EnhancedBot bot, String permanentId, Function<String, Button> configure, Consumer<ButtonInteractionEvent> action) {
        super(bot, permanentId, configure, action);
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        this.complete(event.getComponentId(),event);
    }
}
