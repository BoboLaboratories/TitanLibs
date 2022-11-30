package it.titanet.titanlibs.messages;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Message {
    private final String lines;
    private final List<Replacer> replacers = new ArrayList<>();

    Message(@NotNull String... lines) {
        this.lines = String.join("\n", Arrays.asList(lines));
    }

    void send(@NotNull BukkitAudiences audiences,
                     @NotNull MiniMessage miniMessage,
                     @NotNull CommandSender player) {
        audiences.sender(player).sendMessage(build(miniMessage));
    }

    <T extends Collection<CommandSender>> void send(@NotNull BukkitAudiences audiences,
                                                           @NotNull MiniMessage miniMessage,
                                                           @NotNull T players) {
        for (CommandSender player : players) {
            send(audiences, miniMessage, player);
        }
    }

    public void replace(String from, String to) {
        replacers.add(new Replacer(from, to));
    }

    private @NotNull Component build(@NotNull MiniMessage miniMessage) {

        String message = lines;

        for (Replacer replacer : replacers) {
            message = message.replace(replacer.from(), replacer.to());
        }

        replacers.clear();

        return miniMessage.deserialize(message);
    }

    public record Replacer(String from, String to) {

    }
}
