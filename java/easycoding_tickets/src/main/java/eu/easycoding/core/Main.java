package global.easycoding.core;

import global.easycoding.core.cmd.CreateTicketEmbed;
import global.easycoding.listener.Ticket;
import io.github.cdimascio.dotenv.Dotenv;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommand;


public class Main {
    public  static Dotenv dotenv = Dotenv.load();

    public static void main(String[] args) {
        System.out.println("Loading...");
        DiscordApi api = new DiscordApiBuilder()
                .setToken(dotenv.get("TOKEN"))
                .setAllIntents()
                .login().exceptionally(e -> {
                    System.out.println(e.getMessage());
                    return null;
                }).join();

        api.updateActivity(ActivityType.LISTENING, "Spotify");

        System.out.println("Registering Slash Commands...");

        Server server = api.getServerById(995457939064225832L).orElse(null);
        SlashCommand.with("create-ticket-embed", "creates the ticket embed").createForServer(server).exceptionally(exception -> {
            System.out.println(exception.getMessage());
            return null;
        }).join();

        System.out.println("Done!");

        System.out.println("Loading Listener...");

        api.addListener(new CreateTicketEmbed());
        api.addListener(new Ticket());

        System.out.println("Done!");
    }
}
