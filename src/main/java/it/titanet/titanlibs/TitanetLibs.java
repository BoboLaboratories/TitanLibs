package it.titanet.titanlibs;

import it.titanet.titanlibs.commons.examples.ExampleEnum;
import it.titanet.titanlibs.commons.format.Format;
import it.titanet.titanlibs.paper.messages.Message;
import it.titanet.titanlibs.paper.messages.MessageManager;
import it.titanet.titanlibs.paper.configurations.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class TitanetLibs extends JavaPlugin {
    private ConfigManager<ExampleEnum> configManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        this.configManager = new ConfigManager<>(this, ExampleEnum.class);
        configManager.onEnable();
        FileConfiguration configuration = configManager.getConfig(ExampleEnum.ROOT);
        MessageManager messageManager = new MessageManager(this);
        Message keyMessage = messageManager.fromKey("some-key");
        Message linesMessage = messageManager.fromLines("from", "some", "lines");

        Format.FormatConfiguration formatConfig = Format.FormatConfiguration.fromSpigotConfig(configuration.getConfigurationSection("format"));


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        if (configManager != null) {
            configManager.onDisable();
            configManager = null;
        }

    }
}
