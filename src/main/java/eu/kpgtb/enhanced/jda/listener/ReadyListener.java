package eu.kpgtb.enhanced.jda.listener;

import eu.kpgtb.enhanced.jda.EnhancedBot;
import eu.kpgtb.enhanced.jda.controller.EnhancedListener;
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
