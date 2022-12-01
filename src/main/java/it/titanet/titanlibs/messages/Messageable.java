package it.titanet.titanlibs.messages;

/*
* TODO: Trovare un nome migliore a sta roba
* */

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

public interface Messageable {

    @NotNull MiniMessage getMiniMessage();

    @NotNull BukkitAudiences getAudience();

}
