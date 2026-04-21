package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    // Calea către fișierul cu date — relativă la rădăcina proiectului
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {

        List<Student> studenti = citesteStudentiDinFisier();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();

        if (line == null || line.trim().isEmpty()) {
            // nicio comanda => nu fac nimic
            return;
        }

        line = line.trim();

        if (line.equals("PRINT")) {
            for (Student s : studenti) {
                System.out.println(s);
            }
            return;
        }

        String[] parts = line.split(" ", 2);
        if (parts.length < 2) {
            // sa nu crape nici la comanda invalida
            return;
        }

        String cmd = parts[0];
        String nume = parts[1].trim();

        Student original = null;
        for (Student s : studenti) {
            if (s.getNume().equals(nume)) {
                original = s;
                break;
            }
        }

        if (original == null) return;

        if (cmd.equals("SHALLOW")) {
            Student clona = (Student) original.clone();
            clona.getAdresa().setOras("MODIFICAT");

            System.out.println("Original: " + original);
            System.out.println("Clona: " + clona);
        }

        if (cmd.equals("DEEP")) {
            Student clona = original.deepClone();
            clona.getAdresa().setOras("MODIFICAT");

            System.out.println("Original: " + original);
            System.out.println("Clona: " + clona);
        }
    }

    private static List<Student> citesteStudentiDinFisier() throws Exception {
        List<Student> list = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
        String line;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                // sar peste liniile goale
                continue;
            }

            String[] p = line.split(",");
            if (p.length < 4) {
                // linie invalida, o ignoram ca sa nu crape testele
                continue;
            }

            String nume = p[0].trim();
            int varsta = Integer.parseInt(p[1].trim());
            String oras = p[2].trim();
            String strada = p[3].trim();

            Adresa adresa = new Adresa(oras, strada);
            Student s = new Student(nume, varsta, adresa);
            list.add(s);
        }

        br.close();
        return list;
    }
}
