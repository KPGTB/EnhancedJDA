package dev.projectenhanced.enhancedjda.controller.command.component;

import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.HashMap;
import java.util.Map;

public class ComponentController {
    private final Map<String, EnhancedComponent<?,?>> components;

    public ComponentController() {
        this.components = new HashMap<>();
    }

    public void registerComponents(EnhancedComponent<?,?> ...components) {
        for (EnhancedComponent<?, ?> component : components) {
            this.components.put(component.componentId, component);
        }
    }

    public ItemComponent getComponent(String id) {
        return (ItemComponent) components.get(id).getComponent();
    }

    public Modal getModal(String id) {
        return (Modal) components.get(id).getComponent();
    }
}
