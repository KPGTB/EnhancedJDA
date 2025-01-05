package eu.kpgtb.command;

import dev.projectenhanced.enhancedjda.EnhancedBot;
import dev.projectenhanced.enhancedjda.controller.command.EnhancedCommand;
import dev.projectenhanced.enhancedjda.controller.command.annotation.CommandOption;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class TestCommand extends EnhancedCommand {
    private final EnhancedBot bot;

    public TestCommand(EnhancedBot bot) {
        super(bot);
        this.bot = bot;
    }

    public class Something {
        public void one(SlashCommandInteractionEvent event) {
            event.reply("one").queue();
        }

        public void two(SlashCommandInteractionEvent event) {
            event.reply("two").queue();
        }
    }

    public class Anything {

        @CommandOption(type= OptionType.STRING,name="em",description = "idk")
        @CommandOption(type= OptionType.INTEGER,name="elo",description = "maybe")
        public void three(SlashCommandInteractionEvent event) {
            event.reply("three").queue();
        }
    }
}
