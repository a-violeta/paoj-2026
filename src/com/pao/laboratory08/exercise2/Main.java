package com.pao.laboratory08.exercise2;

import com.pao.laboratory08.exercise1.Adresa;
import com.pao.laboratory08.exercise1.Student;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {

        // citește studenții din fișier
        List<Student> studenti = citesteStudenti();

        // citește pragul din stdin
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) {
            return; // daca nu e input, nu crapa programul
        }
        int prag = sc.nextInt();

        // filtrare
        List<Student> filtrati = new ArrayList<>();
        for (Student s : studenti) {
            if (s.getVarsta() >= prag) {
                filtrati.add(s);
            }
        }

        // scriere in rezultate.txt
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("rezultate.txt"))) {
            for (Student s : filtrati) {
                bw.write(s.toString());
                bw.newLine();
            }
        }

        // afișare sumar
        System.out.println("Filtru: varsta >= " + prag);
        System.out.println("Rezultate: " + filtrati.size() + " studenti");
        System.out.println();

        for (Student s : filtrati) {
            System.out.println(s);
        }

        System.out.println();
        System.out.println("Scris in: rezultate.txt");
    }

    private static List<Student> citesteStudenti() throws Exception {
        List<Student> list = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
        String line;

        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] p = line.split(",");
            if (p.length < 4) continue;

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
