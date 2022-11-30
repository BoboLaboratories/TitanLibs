package it.titanet.titanlibs.utils;

public interface Reloadable {
    default void onEnable() {}

    default void onDisable() {}

    default void reload() {
        onDisable();

        onEnable();
    }
}
