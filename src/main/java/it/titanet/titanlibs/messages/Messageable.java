package it.titanet.titanlibs.messages;

/*
* TODO: Trovare un nome migliore a sta roba
* */

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;

public interface Messageable {

    MiniMessage getMiniMessage();

    BukkitAudiences getAudience();

}
