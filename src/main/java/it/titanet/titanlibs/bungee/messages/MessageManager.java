package it.titanet.titanlibs.bungee.messages;

import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MessageManager {
    private final BungeeAudiences audiences;
    private final MiniMessage miniMessage;
    private Configuration lang;

    public MessageManager(@NotNull Plugin plugin) {
        audiences = BungeeAudiences.create(plugin);
        miniMessage = MiniMessage.miniMessage();

    }

    public MessageManager(@NotNull Plugin plugin, @NotNull Configuration lang) {
        this.lang = lang;
        audiences = BungeeAudiences.create(plugin);
        miniMessage = MiniMessage.miniMessage();
    }

    public @Nullable Message fromKey(@NotNull String key) {
        if (lang != null) {
            String rawMessage = lang.getString(key);
            if (rawMessage != null) {
                return new Message(audiences, miniMessage, rawMessage);
            } else {
                ProxyServer.getInstance().getLogger().severe("La key: " + key + " non è stata trovata nel config");
            }
        } else {
            ProxyServer.getInstance().getLogger().severe("Non è stato inizializzato nessun file di configuratione");
        }
        return null;
    }

    public @NotNull Message fromLines(@NotNull String... lines) {
        return new Message(audiences, miniMessage, lines);
    }

    public void sendFromKey(String key, CommandSender sender, Message.Replacer... replacers) {
        Message message = replaceMessage(key, replacers);
        if (message != null) {
            message.send(sender);
        }
    }

    public <S extends Collection<CommandSender>> void sendFromKey(String key, S senders, Message.Replacer... replacers) {
        Message message = replaceMessage(key, replacers);
        if (message != null) {
            message.send(senders);
        }
    }

    private @Nullable Message replaceMessage(String key, Message.Replacer... replacers) {
        Message message = fromKey(key);

        if (message != null) {
            for (Message.Replacer replacer : replacers) {
                message.replace(replacer.from(), replacer.to());
            }
        }
        return message;
    }


    // Please be kind to humanity and do not use this
    public List<BaseComponent[]> colorize(@NotNull List<String> list) {
        List<BaseComponent[]> result = new ArrayList<>();
        for (String str : list) {
            result.add(colorize(str));
        }
        return result;
    }

    // Please be kind to humanity and do not use this
    public @NotNull BaseComponent[] colorize(@NotNull String text) {
        return fromLines(text).toBaseComponents();
    }


    public @NotNull BungeeAudiences getAudiences() {
        return audiences;
    }

    public @NotNull MiniMessage getMiniMessage() {
        return miniMessage;
    }
}
