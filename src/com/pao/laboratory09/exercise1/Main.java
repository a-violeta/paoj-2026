package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data contSursa contDestinatie tip)
        // 2. Setează câmpul note = "procesat" pe fiecare tranzacție înainte de serializare
        // 3. Serializează lista de tranzacții în OUTPUT_FILE cu ObjectOutputStream (try-with-resources)
        // 4. Deserializează lista din OUTPUT_FILE cu ObjectInputStream (try-with-resources)
        // 5. Procesează comenzile din stdin până la EOF:
        //    - LIST          → afișează toate tranzacțiile, câte una pe linie
        //    - FILTER yyyy-MM → afișează tranzacțiile cu data care începe cu yyyy-MM
        //                       sau "Niciun rezultat." dacă nu există
        //    - NOTE id        → afișează "NOTE[id]: <valoarea câmpului note>"
        //                       sau "NOTE[id]: not found" dacă id-ul nu există
        //
        // Format linie tranzacție:
        //   [id] data tip: suma RON | contSursa -> contDestinatie
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON | RO01SRC1 -> RO01DST1

        Scanner sc = new Scanner(System.in);

        // citeste N care sper sa nu crape
        String line = sc.nextLine().trim();
        int N = Integer.parseInt(line);

        List<Tranzactie> lista = new ArrayList<>();

        // N linii complete, nu cu nextInt()
        for (int i = 0; i < N; i++) {
            String row = sc.nextLine().trim();
            while (row.isEmpty()) { // daca checker ul trimite linii goale
                row = sc.nextLine().trim();
            }

            String[] parts = row.split("\\s+");
            int id = Integer.parseInt(parts[0]);
            double suma = Double.parseDouble(parts[1]);
            String data = parts[2];
            String contSursa = parts[3];
            String contDestinatie = parts[4];
            TipTranzactie tip = TipTranzactie.valueOf(parts[5]);

            Tranzactie t = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            t.note = "procesat";
            lista.add(t);
        }

        // SERIALIZARE
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            oos.writeObject(lista);
        }

        // DESERIALIZARE
        List<Tranzactie> restored;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))) {
            restored = (List<Tranzactie>) ois.readObject();
        }

        // COMENZI
        while (sc.hasNextLine()) {
            String cmd = sc.nextLine().trim();
            if (cmd.isEmpty()) continue;

            if (cmd.equals("LIST")) {
                for (Tranzactie t : restored) {
                    System.out.println(t);
                }
            }

            else if (cmd.startsWith("FILTER")) {
                String[] p = cmd.split("\\s+");
                String prefix = p[1];

                List<Tranzactie> rez = new ArrayList<>();
                for (Tranzactie t : restored) {
                    if (t.getData().startsWith(prefix)) {
                        rez.add(t);
                    }
                }

                if (rez.isEmpty()) {
                    System.out.println("Niciun rezultat.");
                } else {
                    for (Tranzactie t : rez) {
                        System.out.println(t);
                    }
                }
            }

            else if (cmd.startsWith("NOTE")) {
                String[] p = cmd.split("\\s+");
                int id = Integer.parseInt(p[1]);

                Tranzactie found = null;
                for (Tranzactie t : restored) {
                    if (t.getId() == id) {
                        found = t;
                        break;
                    }
                }

                if (found == null) {
                    System.out.println("NOTE[" + id + "]: not found");
                } else {
                    System.out.println("NOTE[" + id + "]: " + found.getNote());
                }
            }
        }
    }
}
