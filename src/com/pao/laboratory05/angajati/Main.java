package com.pao.laboratory05.angajati;

import java.util.Scanner;

/**
 * Exercise 3 — Angajați
 *
 * Cerințele complete se află în:
 *   src/com/pao/laboratory05/Readme.md  →  secțiunea "Exercise 3 — Angajați"
 *
 * Creează fișierele de la zero în acest pachet, apoi rulează Main.java
 * pentru a verifica output-ul așteptat din Readme.
 */
public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AngajatService service = AngajatService.getInstance();
        boolean running = true;

        while (running) {
            System.out.println("\n===== Gestionare Angajați =====");
            System.out.println("1. Adaugă angajat");
            System.out.println("2. Listare după salariu");
            System.out.println("3. Caută după departament");
            System.out.println("0. Ieșire");
            System.out.print("Opțiune: ");
            // citește opțiunea și execută acțiunea

            String option = scanner.nextLine().trim();

            switch (option) {
                case "1" -> {
                    System.out.print("Nume angajat: ");
                    String nume = scanner.nextLine();

                    System.out.print("Departament: ");
                    String numeDept = scanner.nextLine();

                    System.out.print("Locație departament: ");
                    String locatie = scanner.nextLine();

                    System.out.print("Salariu: ");
                    double salariu = Double.parseDouble(scanner.nextLine());

                    Departament d = new Departament(numeDept, locatie);
                    Angajat a = new Angajat(nume, d, salariu);

                    service.addAngajat(a);
                }

                case "2" -> {
                    System.out.println("\n--- Angajați sortați după salariu ---");
                    service.listBySalary();
                }

                case "3" -> {
                    System.out.print("Numele departamentului: ");
                    String dept = scanner.nextLine();
                    service.findByDepartament(dept);
                }

                case "0" -> {
                    System.out.println("La revedere!");
                    running = false;
                }

                default -> System.out.println("Opțiune invalidă!");

            }
        }
    }
}
