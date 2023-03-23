package it.titanet.titanlibs.commons.databases;

import java.io.File;
import java.io.IOException;

public class H2Database extends Database {

    private final File location;

    public H2Database(File path) {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            onDriversNotFound();
        }
        this.location = path;
        if (!path.exists()) {
            try {
                path.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                onDatabaseCreationFail();
            }
        }
    }

    @Override
    protected String getJdbcUrl() {
        return "jdbc:h2:" + location.getAbsolutePath();
    }
}
