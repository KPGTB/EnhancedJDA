package eu.kpgtb.command;

import dev.projectenhanced.enhancedjda.EnhancedBot;
import dev.projectenhanced.enhancedjda.controller.command.EnhancedCommand;
import dev.projectenhanced.enhancedjda.controller.command.annotation.CommandOption;
import dev.projectenhanced.enhancedjda.controller.command.annotation.CommandPermission;
import dev.projectenhanced.enhancedjda.controller.command.annotation.MainCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@CommandPermission({Permission.ADMINISTRATOR})
public class ClearCommand extends EnhancedCommand {
    public ClearCommand(EnhancedBot bot) {
        super(bot);
    }


    @MainCommand
    @CommandOption(type = OptionType.INTEGER, name = "amount", description = "Amount of messages to remove",required = true)
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();
        event.getChannel()
                .getIterableHistory()
                .takeAsync(event.getOption("amount").getAsInt())
                .thenApply(event.getChannel()::purgeMessages);
        event.getHook().sendMessage("Done :3").queue();
    }
}
