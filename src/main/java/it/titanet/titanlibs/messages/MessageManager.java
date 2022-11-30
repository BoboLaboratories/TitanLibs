package it.titanet.titanlibs.messages;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;


public class MessageManager<T extends Plugin & Messageable> {
    private final T plugin;
    private FileConfiguration lang;

    public MessageManager(@NotNull T plugin) {
        this.plugin = plugin;
    }

    public MessageManager(@NotNull T plugin, @NotNull FileConfiguration lang) {
        this.plugin = plugin;
        this.lang = lang;
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
        message.send(plugin.getAudience(), plugin.getMiniMessage(), commandSender);
    }

    public <S extends Collection<CommandSender>> void send(@NotNull Message message, @NotNull S senders) {
        message.send(plugin.getAudience(), plugin.getMiniMessage(), senders);
    }


}
