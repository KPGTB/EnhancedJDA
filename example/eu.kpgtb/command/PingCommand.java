package eu.kpgtb.command;

import dev.projectenhanced.enhancedjda.EnhancedBot;
import dev.projectenhanced.enhancedjda.controller.command.EnhancedCommand;
import dev.projectenhanced.enhancedjda.controller.command.annotation.CommandDescription;
import dev.projectenhanced.enhancedjda.controller.command.annotation.CommandOption;
import dev.projectenhanced.enhancedjda.controller.command.annotation.MainCommand;
import dev.projectenhanced.enhancedjda.controller.command.component.EnhancedButton;
import dev.projectenhanced.enhancedjda.controller.command.component.EnhancedEntitySelect;
import dev.projectenhanced.enhancedjda.controller.command.component.EnhancedModal;
import dev.projectenhanced.enhancedjda.controller.command.component.EnhancedStringSelect;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectInteraction;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.awt.*;
import java.time.Instant;

@CommandDescription("Ping Pong!")
public class PingCommand extends EnhancedCommand {
    private final EnhancedBot bot;

    public PingCommand(EnhancedBot bot) {
        super(bot);
        this.bot = bot;

        bot.getComponentController().registerComponents(
                new EnhancedButton(this.bot,
                        "report_stuff",
                        (id) -> Button.danger(id, "Report something"),
                        (e) -> {
                            e.replyModal(
                                    new EnhancedModal(this.bot,
                                            (id) -> Modal.create(id, "Report something")
                                                    .addActionRow(TextInput.create("subject", "Report subject", TextInputStyle.SHORT).build())
                                                    .addActionRow( TextInput.create("description", "Report description", TextInputStyle.PARAGRAPH).build())
                                                    .build(),
                                            (modalEvent) -> {
                                                modalEvent.reply("# " + modalEvent.getValue("subject").getAsString() + "\n*" + modalEvent.getValue("description").getAsString() + "*").queue();
                                            }
                                    ).getComponent()
                            ).queue();
                        }
                )
        );
    }

    @MainCommand
    @CommandOption(type=OptionType.USER, name="user", description = "User to play with", required = true)
    public void execute(SlashCommandInteractionEvent event) {
        User user = event.getOption("user").getAsUser();

        event.replyEmbeds(
                new EmbedBuilder()
                    .setColor(new Color(220,160,88))
                    .setTitle("\uD83C\uDFD3 Ping Pong!")
                    .setDescription("Playing that awesome game created using EnhancedJDA")
                    .addField("Player", event.getUser().getName(), true)
                    .addField("Opponent", user.getName(),true)
                    .setTimestamp(Instant.now())
                    .setFooter("Made with EnhancedJDA")
                    .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                    .setImage("https://download.kpgtb.eu/projectenhanced_banner.png")
                    .build()
        ).addActionRow(
                new EnhancedButton(this.bot,
                        (id) -> Button.primary(id, "Click me").withEmoji(Emoji.fromUnicode("\uD83E\uDD7A")),
                        (e) -> {
                            e.reply("Yup!").setEphemeral(true).queue();
                        }
                ).getComponent(),
                new EnhancedButton(this.bot,
                        (id) -> Button.link("https://projectenhanced.dev", "Documentation"),
                        (e) -> {}
                ).getComponent(),
                bot.getComponentController().getComponent("report_stuff")
        ).addActionRow(
                new EnhancedStringSelect(this.bot,
                        this::testStringSelect,
                        this::testStringSelect
                ).getComponent()
        ).addActionRow(
                new EnhancedEntitySelect(this.bot, this::testEntitySelect, this::testEntitySelect).getComponent()
        ).queue();
    }

    private EntitySelectMenu testEntitySelect(String id) {
        return EntitySelectMenu.create(id, EntitySelectMenu.SelectTarget.CHANNEL).setRequiredRange(1,3).build();
    }

    private void testEntitySelect(EntitySelectInteraction e) {
        e.reply("You selected: " + String.join(", ",
                        e.getMentions().getChannels().stream().map(GuildChannel::getName).toList()
                )
        ).setEphemeral(true).queue();
    }

    private StringSelectMenu testStringSelect(String id) {
        return StringSelectMenu.create(id)
                .addOption("Something", "sth")
                .addOption("Nothing", "nope")
                .addOption("Maybe", ":/")
                .build();
    }

    private void testStringSelect(StringSelectInteractionEvent e) {
        e.reply("You selected: " + String.join(", ", e.getValues())).setEphemeral(true).queue();
    }

}
