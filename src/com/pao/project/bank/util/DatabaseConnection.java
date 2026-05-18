package com.pao.project.bank.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {

    private Connection connection;

    private DatabaseConnection() throws IOException, SQLException {
        Properties properties = new Properties();

        try (InputStream input =
                     getClass().getClassLoader().getResourceAsStream("resources/db.properties")) {

            if (input == null) {
                throw new IOException("Cannot find db.properties");
            }

            properties.load(input);
        }

        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");

        connection = DriverManager.getConnection(url, user, password);
    }

    private static class Holder {
        private static DatabaseConnection INSTANCE;

        static {
            try {
                INSTANCE = new DatabaseConnection();
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static DatabaseConnection getInstance() {
        return Holder.INSTANCE;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}