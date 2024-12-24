package dev.projectenhanced.enhancedjda.controller.command.component;

import dev.projectenhanced.enhancedjda.EnhancedBot;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.function.Consumer;
import java.util.function.Function;

public class EnhancedModal extends EnhancedComponent<Modal, ModalInteractionEvent>{
    public EnhancedModal(EnhancedBot bot, Function<String, Modal> configure, Consumer<ModalInteractionEvent> action) {
        super(bot, configure, action);
    }

    public EnhancedModal(EnhancedBot bot, String permanentId, Function<String, Modal> configure, Consumer<ModalInteractionEvent> action) {
        super(bot, permanentId, configure, action);
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        this.complete(event.getModalId(),event);
    }
}
