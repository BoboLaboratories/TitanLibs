package it.titanet.titanlibs.commons.databases;

public interface DatabaseEvents {

    default void onDatabaseCreationFail() {
    }

    default void onDriversNotFound() {

    }

}
