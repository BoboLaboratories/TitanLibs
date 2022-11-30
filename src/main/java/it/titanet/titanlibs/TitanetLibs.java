package it.titanet.titanlibs;

import it.titanet.titanlibs.configurations.ConfigManager;
import it.titanet.titanlibs.databases.H2Database;
import it.titanet.titanlibs.examples.ExampleEnum;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class TitanetLibs extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        ConfigManager<ExampleEnum> configManager = new ConfigManager<>(this, ExampleEnum.class);
        configManager.onEnable();

//        ConfigurationSection dbSettings = getConfig().getConfigurationSection("database");
        H2Database database = new H2Database(new File(getDataFolder().getAbsolutePath(), "databse"));

        database.onEnable();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
