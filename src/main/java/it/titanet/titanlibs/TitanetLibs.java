package it.titanet.titanlibs;

import it.titanet.titanlibs.configurations.ConfigManager;
import it.titanet.titanlibs.examples.ExampleEnum;
import it.titanet.titanlibs.messages.Message;
import it.titanet.titanlibs.messages.MessageManager;
import it.titanet.titanlibs.messages.Messageable;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class TitanetLibs extends JavaPlugin implements Messageable {
    private ConfigManager<ExampleEnum> configManager;

    private static MiniMessage miniMessage;
    private static BukkitAudiences audience;

    @Override
    public void onEnable() {
        // Plugin startup logic

        this.configManager = new ConfigManager<>(this, ExampleEnum.class);
        configManager.onEnable();
        FileConfiguration configuration = configManager.getConfig(ExampleEnum.ROOT);
        MessageManager messageManager = new MessageManager(this);
        Message keyMessage = messageManager.fromKey("some-key");
        Message linesMessage = messageManager.fromLines("from", "some", "lines");


    }

    @Override
    public void onDisable() {

        if (configManager != null) {
            configManager.onDisable();
            configManager = null;
        }

        // Plugin shutdown logic
    }

    @Override
    public MiniMessage getMiniMessage() {
        return null;
    }

    @Override
    public BukkitAudiences getAudience() {
        return null;
    }
}
