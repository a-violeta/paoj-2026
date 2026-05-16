package com.pao.project.bank.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConnection {

    private final Connection connection;

    private DatabaseConnection() {

        try {

            Properties properties = new Properties();

            InputStream input =
                    getClass()
                            .getClassLoader()
                            .getResourceAsStream("db.properties");

            if (input == null) {
                throw new RuntimeException("db.properties not found");
            }

            properties.load(input);

            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");

            connection =
                    DriverManager.getConnection(url, user, password);

            System.out.println("Database connected.");

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to initialize database connection",
                    e
            );
        }
    }

    private static class Holder {

        private static final DatabaseConnection INSTANCE =
                new DatabaseConnection();
    }

    public static DatabaseConnection getInstance() {
        return Holder.INSTANCE;
    }

    public Connection getConnection() {
        return connection;
    }
}