package it.titanet.titanlibs.configurations;

import it.titanet.titanlibs.utils.Reloadable;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

/*
* Non fate domande su sta classe
* non la capisco nemmeno io :D
* */

public final class ConfigManager<T extends Enum<T> & IConfig> implements Reloadable {

    private final Plugin plugin;
    private final Map<T, FileConfiguration> configurations;
    private final Class<T> clazz;

    public ConfigManager(@NotNull Plugin api, Class<T> clazz) {
        this.clazz = clazz;
        this.configurations = new EnumMap<>(clazz);
        this.plugin = api;
    }

    @Override
    public void onEnable() {
        for (T config : EnumSet.allOf(clazz)) {
            String path = config.getPath();
            Bukkit.getLogger().info("Loading: " + path);
            configurations.put(config, load(path));
        }
    }

    @Override
    public void onDisable() {
        configurations.clear();
    }

    public @NotNull FileConfiguration getConfig(@NotNull T config) {
        return configurations.get(config);
    }


    private @NotNull FileConfiguration load(@NotNull String relativePath) {
        File file = new File(plugin.getDataFolder(), relativePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(relativePath, false);
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public void saveResource(@NotNull String relativePath) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        File configFile = new File(plugin.getDataFolder(), relativePath);

        try (OutputStream outputStream = new FileOutputStream(configFile)) {
            InputStream in = plugin.getResource(relativePath);
            if (in != null) {
                in.transferTo(outputStream);
            } else {
                Bukkit.getLogger().severe("Resource not found: " + relativePath);
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}