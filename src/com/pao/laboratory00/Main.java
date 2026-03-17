package com.pao.laboratory00;

import java.util.Scanner;

/**
 *  Rezolvați următoarele exerciții în fișierele
 *  1 MediaAritmetica.java și
 *  2 DiagonaleleMatricei.java din pachetul com.pao.loborator00.
 *
 * 1. Cititi de la tastatura un sir cu n elemente intregi.
 * Afisati sirul si media aritmetica a elementelor sirului.
 *
 * 2. Cititi de la tastatura o matrice de n ori n elemente REALE.
 * Afisati matricea in consola, apoi suma elementelor de pe diagonala principala
 *    si produsul elementelor de pe diagonala secundara.
 *
 */

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Exercitiul 1 ===");
        MediaAritmetica.rezolva(scanner);

        System.out.println("\n=== Exercitiul 2 ===");
        DiagonaleleMatricei.rezolva(scanner);

        scanner.close();
    }
}
