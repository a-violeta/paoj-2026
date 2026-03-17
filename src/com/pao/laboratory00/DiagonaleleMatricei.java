package com.pao.laboratory00;

/**
 * Exercitiul 2
 *
 * Cititi de la tastatura o matrice de n ori n elemente REALE.
 *
 * 1. Afisati matricea in consola.
 * 2. Afisati suma elementelor de pe diagonala principala
 *    si produsul elementelor de pe diagonala secundara.
 *
 */

import java.util.Scanner;

public class DiagonaleleMatricei {
    //public static void main(String[] args) {
    public static void rezolva(Scanner scanner){
        //Scanner scanner = new Scanner(System.in);

        // Citim dimensiunea matricei
        System.out.print("Introduceti n (dimensiunea matricei n x n): ");
        int n = scanner.nextInt();

        double[][] a = new double[n][n];

        // Citim elementele matricei
        System.out.println("Introduceti elementele matricei (linie cu linie):");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = scanner.nextDouble();
            }
        }

        System.out.println("Matricea introdusa este:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(a[i][j] + "\t");
            }
            System.out.println();
        }


        double sumaDiagPrincipala = 0;
        double produsDiagSecundara = 1;

        for (int i = 0; i < n; i++) {
            sumaDiagPrincipala += a[i][i];

            produsDiagSecundara *= a[i][n - 1 - i];
        }

        System.out.println("Suma elementelor de pe diagonala principala: " + sumaDiagPrincipala);
        System.out.println("Produsul elementelor de pe diagonala secundara: " + produsDiagSecundara);

        //scanner.close();
    }
}
