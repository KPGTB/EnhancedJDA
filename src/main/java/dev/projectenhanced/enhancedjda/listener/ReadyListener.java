package dev.projectenhanced.enhancedjda.listener;

import dev.projectenhanced.enhancedjda.EnhancedBot;
import dev.projectenhanced.enhancedjda.controller.listener.EnhancedListener;
import net.dv8tion.jda.api.events.session.ReadyEvent;

public class ReadyListener extends EnhancedListener {
    private final EnhancedBot bot;

    public ReadyListener(EnhancedBot bot) {
        super(bot);
        this.bot = bot;
    }

    @Override
    public void onReady(ReadyEvent event) {
        this.bot.onReady(event);
    }
}
