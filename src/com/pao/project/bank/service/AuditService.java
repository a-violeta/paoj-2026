package com.pao.project.bank.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class AuditService {

    private static final String AUDIT_FILE = "audit.csv";

    private AuditService() {
    }

    private static class Holder {

        private static final AuditService INSTANCE =
                new AuditService();
    }

    public static AuditService getInstance() {
        return Holder.INSTANCE;
    }

    public synchronized void logAction(String actionName) {

        try (FileWriter writer =
                     new FileWriter(AUDIT_FILE, true)) {

            writer.write(
                    actionName + "," +
                            LocalDateTime.now() + "\n"
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to write audit log",
                    e
            );
        }
    }
}