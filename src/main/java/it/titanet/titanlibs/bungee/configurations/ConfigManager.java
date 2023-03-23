package it.titanet.titanlibs.bungee.configurations;

import it.titanet.titanlibs.commons.configurations.IConfig;
import it.titanet.titanlibs.commons.utils.Reloadable;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
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
    private final Map<T, Configuration> configurations;
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
            ProxyServer.getInstance().getLogger().info("Loading: " + path);
            configurations.put(config, load(path));
        }
    }

    @Override
    public void onDisable() {
        configurations.clear();
    }

    public @NotNull Configuration getConfig(@NotNull T config) {
        return configurations.get(config);
    }


    private @NotNull Configuration load(@NotNull String relativePath) {
        File file = new File(plugin.getDataFolder(), relativePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();

            saveResource(relativePath);
        }
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void saveResource(@NotNull String relativePath) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        File configFile = new File(plugin.getDataFolder(), relativePath);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(configFile);
            InputStream in = plugin.getResourceAsStream(relativePath);
            in.transferTo(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}