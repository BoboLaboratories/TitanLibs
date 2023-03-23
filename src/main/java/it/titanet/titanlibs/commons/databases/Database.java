package it.titanet.titanlibs.commons.databases;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import it.titanet.titanlibs.commons.utils.Reloadable;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public abstract class Database implements Reloadable, DatabaseEvents {
    private final HikariConfig config;
    private HikariDataSource dataSource;

    protected Database() {
        this.config = new HikariConfig();
    }

    @Override
    public final void onEnable() {
        config.setJdbcUrl(getJdbcUrl());
        config.setUsername(getUsername());
        config.setPassword(getPassword());
        config.setDataSourceProperties(getDataSourceProperties());
        dataSource = new HikariDataSource(config);

        try {
            createDatabase();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            onDatabaseCreationFail();
        }
    }


    @Override
    public final void onDisable() {
        dataSource.close();
    }


    public final @NotNull Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    protected abstract String getJdbcUrl();

    protected @NotNull String getUsername() {
        return "";
    }

    protected @NotNull String getPassword() {
        return "";
    }

    protected @NotNull Properties getDataSourceProperties() {
        return new Properties();
    }

    // Grazie Bobo
    protected @NotNull List<String> loadSchema() throws IOException {
        List<String> queries = new LinkedList<>();
        InputStream inputStream = getClass().getResourceAsStream("/schema.sql");
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("-- SEPARATOR --")) {
                        String query = sb.toString().trim();
                        if (!StringUtils.isBlank(query)) {
                            queries.add(query);
                        }
                        sb = new StringBuilder();
                    } else {
                        sb.append(line).append("\n");
                    }
                }
            }
        }

        return queries;
    }

    private void createDatabase() throws SQLException, IOException {
        try (Connection conn = getConnection()) {
            try (Statement statement = conn.createStatement()) {
                conn.setAutoCommit(false);
                List<String> queries = loadSchema();
                for (String query : queries) {
                    statement.addBatch(query);
                }

                statement.executeBatch();
                conn.commit();
                conn.setAutoCommit(true);
            }
        }
    }

}
