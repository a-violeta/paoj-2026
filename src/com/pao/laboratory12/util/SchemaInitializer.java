package com.pao.laboratory12.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

public class SchemaInitializer {

    public static void init(Connection connection) {
        try {
            InputStream is = SchemaInitializer.class
                    .getClassLoader()
                    .getResourceAsStream("schema.sql");

            if (is == null) {
                throw new RuntimeException("Nu gasesc schema.sql in resources!");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            StringBuilder sql = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }

            // execut scriptul
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(sql.toString());
            }

            System.out.println("Schema SQL initializata cu succes!");

        } catch (Exception e) {
            throw new RuntimeException("Eroare la initializarea schemei", e);
        }
    }
}