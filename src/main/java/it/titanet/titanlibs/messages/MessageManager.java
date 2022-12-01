package it.titanet.titanlibs.messages;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;


public class MessageManager {
    private final BukkitAudiences audience;
    private final MiniMessage miniMessage;
    private FileConfiguration lang;


    public MessageManager(@NotNull Plugin plugin) {
        audience = BukkitAudiences.create(plugin);
        miniMessage = MiniMessage.miniMessage();

    }

    public MessageManager(@NotNull Plugin plugin, @NotNull FileConfiguration lang) {
        this.lang = lang;
        audience = BukkitAudiences.create(plugin);
        miniMessage = MiniMessage.miniMessage();
    }

    public @Nullable Message fromKey(@NotNull String key) {
        if (lang != null) {
            String rawMessage = lang.getString(key);
            if (rawMessage != null) {
                return new Message(rawMessage);
            } else {
                Bukkit.getLogger().severe("La key: " + key + " non è stata trovata nel config");
            }
        } else {
            Bukkit.getLogger().severe("Non è stato inizializzato nessun file di configuratione");
        }
        return null;
    }

    public @NotNull Message fromLines(@NotNull String... lines) {
        return new Message(lines);
    }

    public void send(@NotNull Message message, @NotNull CommandSender commandSender) {
        message.send(audience, miniMessage, commandSender);
    }

    public <S extends Collection<CommandSender>> void send(@NotNull Message message, @NotNull S senders) {
        message.send(audience, miniMessage, senders);
    }

    public BukkitAudiences getAudience() {
        return audience;
    }

    public MiniMessage getMiniMessage() {
        return miniMessage;
    }
}
