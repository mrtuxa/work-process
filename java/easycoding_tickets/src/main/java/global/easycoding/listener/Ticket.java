package global.easycoding.listener;

import org.javacord.api.entity.channel.ChannelCategory;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerTextChannelBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.ButtonClickEvent;
import org.javacord.api.listener.interaction.ButtonClickListener;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;

public class Ticket implements ButtonClickListener {
    @Override
    public void onButtonClick(ButtonClickEvent event) {
       if (event.getButtonInteraction().equals("openTicket")) {
           Server server = event.getApi().getServerById(995457939064225832L).orElse(null);
           String roles = String.valueOf(event.getInteraction().getUser().getRoles(server));
           if (!roles.contains("Tickets")) {
               int min = 1000;
               int max = 99999;
               int random_int = (int) Math.floor(Math.random() * (max - min + 1 + min));
               SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
               Date date = new Date();

               String user = event.getInteraction().getUser().getName();
               EmbedBuilder embed = new EmbedBuilder();
               embed.setColor(Color.GREEN);
               embed.setTitle(user + "'s Ticket");
               embed.setDescription("Ein Team Member wird sich um dein Ticket kümmern, bitte habe einen Moment Geduld.");
               embed.setDescription("Datum: " + format.format(date));
               server.createTextChannelBuilder()
                       .setName("ticket-" + user + "-" + random_int)
                       .setCategory(server.getChannelCategoryById(997617070592245822L).orElse(null))
                       .addPermissionOverwrite(event.getInteraction().getUser(), (Permissions) EnumSet.of(PermissionType.VIEW_CHANNEL, PermissionType.ATTACH_FILE, PermissionType.SEND_MESSAGES, PermissionType.USE_APPLICATION_COMMANDS))
                       .addPermissionOverwrite(event.getApi().getRoleById(997618897060642846L).orElse(null), (Permissions) EnumSet.of(PermissionType.VIEW_CHANNEL, PermissionType.ATTACH_FILE, PermissionType.SEND_MESSAGES, PermissionType.USE_APPLICATION_COMMANDS))
                       .create().thenAccept(channel -> {
                           CompletableFuture<Message> ticket = new MessageBuilder()
                                   .addComponents(ActionRow.of(
                                           Button.danger("closeTicket", "Ticket schließen"),
                                           Button.primary("claimTicket", "Ticket claimen")))
                                   .send(channel);
                       });
               EmbedBuilder embedTeam = new EmbedBuilder();
                embedTeam.setColor(Color.RED);
                embedTeam.setTitle("Ticket System");
                embedTeam.setDescription("|| <@&997618897060642846> ||" + "\n\nEs wurde ein neues Ticket erstellt, bitte klicke auf den Button um das Ticket zu claimen");
                embedTeam.setDescription("Datum: " + format.format(date));
                embedTeam.setFooter( "Ticket System", event.getApi().getYourself().getAvatar());
                server.getChannelById("996839235837513878L").orElse(null).asTextChannel().ifPresent(channel -> {
                    channel.sendMessage(embedTeam);
                });
           } else if (roles.contains("Ticket Block")) {
               SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
               Date date = new Date();
               EmbedBuilder embed = new EmbedBuilder();
               embed.setTitle("Ticket System");
               embed.setDescription("Du hast eine Ticket sperre aktiv, bitte warte bis die Sperre aufgehoben wurde.");
               embed.setDescription("Datum: " + format.format(date));
               embed.setColor(Color.RED);
               embed.setFooter( "Ticket System", event.getApi().getYourself().getAvatar());
               event.getInteraction().createImmediateResponder().addEmbed(embed).setFlags(MessageFlag.EPHEMERAL).respond();


               EmbedBuilder team = new EmbedBuilder()
                       .setTitle("Ticket System")
                       .addField("Ticket Sperre", "Der User " + event.getInteraction().getUser().getName() + " hat eine Ticket Sperre wenn du lust hast kannst du dich ihn pflegen", false)
                       .setDescription("Date: " + format.format(date))
                       .setThumbnail(event.getInteraction().getUser().getAvatar());
                server.getChannelById("996839235837513878L").orElse(null).asTextChannel().ifPresent(channel -> {
                    channel.sendMessage(team);
                });
           } else if (event.getInteraction().equals("closeTicket")) {
               SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
               Date date = new Date();
               String close = String.valueOf(event.getInteraction().getUser().getRoles(server));
               if (close.contains("Tickets")) {
                   event.getInteraction().getChannel().ifPresent(channel -> {
                       // delet channel
                       channel.getApi().getTextChannelById(String.valueOf(channel)).ifPresent(textChannel -> {
                           textChannel.as(ServerChannel.class).ifPresent(serverChannel -> {
                               serverChannel.delete();
                           });
                       });

                   });
                   EmbedBuilder log = new EmbedBuilder();
                   log.setTitle("Ticket System");
                   log.setDescription("Team Member " + event.getInteraction().getUser().getName() + " hat das Ticket geschlossen");
                   log.setDescription("Datum: " + format.format(date));
                   log.setColor(Color.RED);
                   log.setFooter( "Ticket System", event.getApi().getYourself().getAvatar());
                   server.getChannelById("996839235837513878L").orElse(null).asTextChannel().ifPresent(channel -> {
                         channel.sendMessage(log);
                     });

               } else {
                   EmbedBuilder embed = new EmbedBuilder();
                   embed.setTitle("Ticket System");
                   embed.setDescription("Du hast keine Berechtigung um dieses Ticket zu schließen.");
                   embed.setColor(Color.RED);
                   embed.setFooter("Ticket System", event.getApi().getYourself().getAvatar());
                   event.getInteraction().createImmediateResponder().addEmbed(embed).setFlags(MessageFlag.EPHEMERAL).respond();
               }
           } else if (event.getButtonInteraction().equals("claimTicket")) {
               if (event.getInteraction().getUser().getRoles(server).contains("Tickets")) {
                   SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                   Date date = new Date();
                   TextChannel channel = event.getInteraction().getChannel().orElse(null);
                   EmbedBuilder embed = new EmbedBuilder();
                   embed.setTitle("Ticket System");
                   embed.setDescription("Ein Team Member wurde dir zugwiesen, er kann jetzt dein Ticket bearbeiten und bringt dir Schokolade und die passende Lösung.");
                   embed.setColor(Color.GREEN);
                   embed.setFooter("Ticket System", event.getApi().getYourself().getAvatar());
                   channel.sendMessage(embed);
                   EmbedBuilder embedTeamFinish = new EmbedBuilder();
                   embedTeamFinish.setTitle("Ticket System");
                   embedTeamFinish.setDescription("|| <@&997618897060642846> ||");
                   embedTeamFinish.setColor(Color.GREEN);
                   embedTeamFinish.addField("Ticket Claim", "<@&997618897060642846> Ticket wurde bereits geclaimed", false);
                   embedTeamFinish.addField("Datum: " + format.format(date), "", false);
                   server.getTextChannelById("996839235837513878").orElse(null).sendMessage(embedTeamFinish);

               }
           }
       }
    }
}
