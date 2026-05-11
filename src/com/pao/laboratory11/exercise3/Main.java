package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // --- 1. Date demo (hardcodate, conform cerintei) ---
        List<Transaction> data = List.of(
                new Transaction(1, new BigDecimal("200.00"), LocalDate.parse("2026-05-01"), "RO", "WEB"),
                new Transaction(2, new BigDecimal("300.00"), LocalDate.parse("2026-05-01"), "RO", "ATM"),
                new Transaction(3, new BigDecimal("50.00"),  LocalDate.parse("2026-05-10"), "NL", "APP"),
                new Transaction(4, new BigDecimal("900.00"), LocalDate.parse("2026-06-02"), "RO", "WEB"),
                new Transaction(5, new BigDecimal("1200.00"),LocalDate.parse("2026-06-03"), "DE", "CRYPTO")
        );

        // --- 2. Construim snapshot-ul imutabil cu collector custom ---
        Snapshot snap = data.stream()
                .collect(CustomCollectors.toSnapshot(3)); // top 3 tranzactii

        // --- 3. Interogare 1: Top tranzactii ---
        System.out.println("=== Top Transactions ===");
        snap.getTopTransactions().forEach(tx ->
                System.out.println("ID=" + tx.getId() + " amount=" + tx.getAmount())
        );

        // --- 4. Interogare 2: Count by country ---
        System.out.println("\n=== Count by Country ===");
        snap.getCountByCountry().entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .forEach(e -> System.out.println(e.getKey() + " -> " + e.getValue()));

        // --- 5. Interogare 3: Count by channel ---
        System.out.println("\n=== Count by Channel ===");
        snap.getCountByChannel().entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .forEach(e -> System.out.println(e.getKey() + " -> " + e.getValue()));

        // --- 6. Interogare 4: Total amount ---
        System.out.println("\n=== Total Amount ===");
        System.out.println(snap.getTotalAmount());
    }
}
