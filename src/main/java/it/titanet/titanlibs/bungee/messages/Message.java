package it.titanet.titanlibs.bungee.messages;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Message {

    private final BungeeAudiences audiences;
    private final MiniMessage miniMessage;
    private final String lines;
    private final List<Replacer> replacers = new ArrayList<>();

    Message(@NotNull BungeeAudiences audiences, @NotNull MiniMessage miniMessage, @NotNull String... lines) {
        this.audiences = audiences;
        this.miniMessage = miniMessage;
        this.lines = String.join("\n", Arrays.asList(lines));
    }

    public void send(@NotNull CommandSender player) {
        audiences.sender(player).sendMessage(build(miniMessage));
    }

    public <T extends Collection<CommandSender>> void send(@NotNull T players) {
        for (CommandSender player : players) {
            send(player);
        }
    }

    public Message replace(String from, String to) {
        replacers.add(new Replacer(from, to));
        return this;
    }

    // Please be kind to humanity and do not use this
    public @NotNull BaseComponent[] toBaseComponents() {
        return BungeeComponentSerializer.get().serialize(build(miniMessage));
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
