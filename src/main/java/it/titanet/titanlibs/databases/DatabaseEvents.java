package it.titanet.titanlibs.databases;

public interface DatabaseEvents {

    default void onDatabaseCreationFail() {
    }

    default void onDriversNotFound() {

    }

}
