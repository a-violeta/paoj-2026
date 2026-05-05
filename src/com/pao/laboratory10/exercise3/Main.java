package com.pao.laboratory10.exercise3;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        // Minim 10 tranzacții, 3 luni, CREDIT + DEBIT
        List<Tranzactie> lista = List.of(
                new Tranzactie(1, 1500.00, "2026-01-15", "CREDIT", "RO01BANK"),
                new Tranzactie(2, 750.50, "2026-01-22", "DEBIT", "RO02BANK"),
                new Tranzactie(3, 200.00, "2026-02-05", "CREDIT", "RO01BANK"),
                new Tranzactie(4, 1200.00, "2026-02-18", "DEBIT", "RO03BANK"),
                new Tranzactie(5, 500.00, "2026-03-10", "CREDIT", "RO04BANK"),
                new Tranzactie(6, 300.00, "2026-03-12", "DEBIT", "RO02BANK"),
                new Tranzactie(7, 900.00, "2026-01-05", "CREDIT", "RO01BANK"),
                new Tranzactie(8, 100.00, "2026-02-20", "DEBIT", "RO04BANK"),
                new Tranzactie(9, 2500.00, "2026-03-25", "CREDIT", "RO05BANK"),
                new Tranzactie(10, 50.00, "2026-03-28", "DEBIT", "RO01BANK")
        );

         // ------------------------------------------------------------
        System.out.println("=== 1. Toate tranzacțiile CREDIT ===");
        lista.stream()
                .filter(t -> t.getTip().equals("CREDIT"))
                .forEach(System.out::println);

        // ------------------------------------------------------------
        System.out.println("\n=== 2. Total procesat ===");
        double total = lista.stream()
                .mapToDouble(Tranzactie::getSuma)
                .sum();
        System.out.printf(Locale.US, "Total procesat: %.2f RON%n", total);

        // ------------------------------------------------------------
        System.out.println("\n=== 3. Sume totale per lună ===");
        Map<String, Double> perLuna = lista.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.summingDouble(Tranzactie::getSuma)
                ));

        perLuna.forEach((luna, suma) ->
                System.out.printf(Locale.US, "%s: %.2f RON%n", luna, suma));

        // ------------------------------------------------------------
        System.out.println("\n=== 4. Top 3 tranzacții ===");
        lista.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3)
                .forEach(System.out::println);

        // ------------------------------------------------------------
        System.out.println("\n=== 5. Conturi sursă unice ===");
        List<String> conturi = lista.stream()
                .map(Tranzactie::getContSursa)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Conturi sursa unice: " + conturi);

        // ------------------------------------------------------------
        System.out.println("\n=== 6. Suma medie ===");
        double medie = lista.stream()
                .mapToDouble(Tranzactie::getSuma)
                .average()
                .orElse(0.0);
        System.out.printf(Locale.US, "Suma medie: %.2f RON%n", medie);

        // ------------------------------------------------------------
        System.out.println("\n=== 7. Extras de cont lunar ===");
        Map<String, List<Tranzactie>> grupat = lista.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.toList()
                ));

        for (var entry : grupat.entrySet()) {
            String luna = entry.getKey();
            List<Tranzactie> tranz = entry.getValue();

            double totalLuna = tranz.stream()
                    .mapToDouble(Tranzactie::getSuma)
                    .sum();

            System.out.printf(Locale.US,
                    "EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n",
                    luna, tranz.size(), totalLuna);
        }
    }
}
