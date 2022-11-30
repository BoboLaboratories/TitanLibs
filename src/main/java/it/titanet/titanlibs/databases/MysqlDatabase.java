package it.titanet.titanlibs.databases;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;

import static java.util.Objects.requireNonNull;

class MysqlDatabase extends Database {
    private final ConfigurationSection config;

    public MysqlDatabase(ConfigurationSection config) {
        this.config = config;
    }

    @Override
    protected @NotNull String getJdbcUrl() {
        return "jdbc:mysql://" + config.getString("host")
                + ":" + config.getString("port")
                + "/" + config.getString("name");
    }

    @Override
    protected @NotNull String getUsername() {
        return requireNonNull(config.getString("username"));
    }

    @Override
    protected @NotNull String getPassword() {
        return requireNonNull(config.getString("password"));
    }

    @Override
    protected @NotNull Properties getDataSourceProperties() {
        Properties properties = new Properties();
        ConfigurationSection propertiesSection = config.getConfigurationSection("properties");
        for (String key : requireNonNull(propertiesSection).getKeys(false)) {
            String value = requireNonNull(propertiesSection.getString(key));
            properties.setProperty(key, value);
        }
        return properties;
    }


}
