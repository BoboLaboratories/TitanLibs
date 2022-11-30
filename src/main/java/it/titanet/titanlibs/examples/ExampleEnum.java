package it.titanet.titanlibs.examples;

import it.titanet.titanlibs.configurations.IConfig;
import org.jetbrains.annotations.NotNull;

public enum ExampleEnum implements IConfig {

    ROOT("config.yml"),
    CONFIG_1("configs/config_1.yml"),
    CONFIG_2("configs/config_2.yml");


    private final String path;
    ExampleEnum(String path) {
        this.path = path;
    }

    @Override
    public @NotNull String getPath() {
        return path;
    }
}
