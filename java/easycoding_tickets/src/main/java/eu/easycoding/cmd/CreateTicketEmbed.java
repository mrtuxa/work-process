package global.easycoding.cmd;

import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

import java.awt.*;

public class CreateTicketEmbed implements SlashCommandCreateListener {
    @Override
    public void onSlashCommandCreate(SlashCommandCreateEvent slashCommandCreateEvent) {
        SlashCommandInteraction interaction = slashCommandCreateEvent.getSlashCommandInteraction();

        if (interaction.getCommandName().equals("create-ticket-embed")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.BLUE);
            embed.setTitle("Ticket System");
            embed.setDescription("Klicke auf den Button um ein :tickets: Ticket zu erstellen");
            embed.setFooter( "Ticket System", slashCommandCreateEvent.getApi().getYourself().getAvatar());
            interaction.createImmediateResponder().addEmbed(embed).addComponents(ActionRow.of(Button.primary("openTicket",  "Ticket erstellen"))).respond();
        }
    }
}
