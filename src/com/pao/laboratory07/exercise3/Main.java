package com.pao.laboratory07.exercise3;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in).useLocale(Locale.US);
        Locale.setDefault(Locale.US);

        int N = sc.nextInt();
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            String tip = sc.next();

            switch (tip) {
                case "STANDARD" -> {
                    String nume = sc.next();
                    double pret = sc.nextDouble();
                    String client = sc.next();
                    comenzi.add(new ComandaStandard(nume, pret, client));
                }
                case "DISCOUNTED" -> {
                    String nume = sc.next();
                    double pret = sc.nextDouble();
                    int disc = sc.nextInt();
                    String client = sc.next();
                    comenzi.add(new ComandaRedusa(nume, pret, disc, client));
                }
                case "GIFT" -> {
                    String nume = sc.next();
                    String client = sc.next();
                    comenzi.add(new ComandaGratuita(nume, client));
                }
            }
        }

        // Afișare comenzi
        comenzi.forEach(c -> System.out.println(c.descriere()));
        System.out.println();

        // Comenzi interactive
        while (true) {
            String cmd = sc.next();

            switch (cmd) {

                case "STATS" -> {
                    System.out.println("--- STATS ---");

                    Map<String, Double> medii = comenzi.stream()
                            .collect(Collectors.groupingBy(
                                    c -> c.getClass().getSimpleName(),
                                    Collectors.averagingDouble(Comanda::pretFinal)
                            ));

                    medii.forEach((k, v) -> {
                        String tip = switch (k) {
                            case "ComandaStandard" -> "STANDARD";
                            case "ComandaRedusa" -> "DISCOUNTED";
                            case "ComandaGratuita" -> "GIFT";
                            default -> k;
                        };
                        System.out.printf("%s: medie = %.2f lei%n", tip, v);
                    });
                }

                case "FILTER" -> {
                    double threshold = sc.nextDouble();
                    System.out.printf("--- FILTER (>= %.2f) ---%n", threshold);

                    comenzi.stream()
                            .filter(c -> c.pretFinal() >= threshold)
                            .forEach(c -> System.out.println(c.descriere()));
                }

                case "SORT" -> {
                    System.out.println("--- SORT (by client, then by pret) ---");

                    comenzi.stream()
                            .sorted(Comparator
                                    .comparing(Comanda::getClient)
                                    .thenComparing(Comanda::pretFinal))
                            .forEach(c -> System.out.println(c.descriere()));
                }

                case "SPECIAL" -> {
                    System.out.println("--- SPECIAL (discount > 15%) ---");

                    comenzi.stream()
                            .filter(c -> c instanceof ComandaRedusa cr && cr.getDiscountProcent() > 15)
                            .forEach(c -> System.out.println(c.descriere()));
                }

                case "QUIT" -> {
                    return;
                }
            }
        }
    }
}
