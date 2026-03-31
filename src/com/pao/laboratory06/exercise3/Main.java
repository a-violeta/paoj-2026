package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        System.out.println("=== CONSTANTE FINANCIARE ===");
        System.out.println("TVA = " + ConstanteFinanciare.TVA.getValoare());

        System.out.println("\n=== INGINERI ===");
        Inginer[] ingineri = {
                new Inginer("Popescu", "Ana", "0722000000", 8000),
                new Inginer("Ionescu", "Vlad", "0722333444", 7000),
                new Inginer("Enache", "Paul", null, 9000)
        };

        System.out.println("\nSortare naturală (după nume):");
        Arrays.sort(ingineri);
        for (Inginer i : ingineri) System.out.println(i);

        System.out.println("\nSortare după salariu descrescător:");
        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        for (Inginer i : ingineri) System.out.println(i);

        System.out.println("\n=== DEMONSTRAȚIE POLIMORFISM ===");
        PlataOnline p = ingineri[0];
        p.autentificare("user1", "parola123");
        System.out.println("Sold: " + p.consultareSold());
        p.efectuarePlata(500);

        System.out.println("\n=== PERSOANĂ JURIDICĂ ===");
        PersoanaJuridica firma = new PersoanaJuridica("TechCorp", "SRL", "0744555666");
        PlataOnlineSMS psms = firma;

        psms.autentificare("firma1", "pass");
        psms.efectuarePlata(2000);

        System.out.println("Trimitere SMS valid: " + psms.trimiteSMS("Plata efectuata"));
        System.out.println("Trimitere SMS invalid: " + psms.trimiteSMS(""));

        System.out.println("SMS-uri trimise: " + firma.getSmsTrimise());

        System.out.println("\n=== EDGE CASES ===");

        try {
            p.autentificare(null, "123");
        } catch (Exception e) {
            System.out.println("Eroare autentificare: " + e.getMessage());
        }

        PersoanaJuridica faraTelefon = new PersoanaJuridica("NoPhone", "SRL", "");
        System.out.println("SMS fără telefon: " + faraTelefon.trimiteSMS("Salut"));
    }
}
