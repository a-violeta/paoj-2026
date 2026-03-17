package com.pao.laboratory00;

/**
 * Exercitiul 1
 *
 * Cititi de la tastatura un sir cu n elemente intregi.
 *
 * 1. Afisati elementele sirului in doua modalitati.
 * 2. Afisati media aritmetica a elementelor sirului.
 *
 */

import java.util.Scanner;

public class MediaAritmetica {
    //public static void main(String[] args) {
    public static void rezolva(Scanner scanner){
        //Scanner scanner = new Scanner(System.in);


        System.out.print("Introduceti numarul de elemente n: ");
        int n = scanner.nextInt();

        int[] sir = new int[n];


        System.out.println("Introduceti cele " + n + " elemente:");
        for (int i = 0; i < n; i++) {
            sir[i] = scanner.nextInt();
        }


        System.out.println("Afisare pe o singura linie:");
        for (int x : sir) {
            System.out.print(x + " ");
        }
        System.out.println();

        System.out.println("Afisare pe linii separate:");
        for (int x : sir) {
            System.out.println(x);
        }


        double suma = 0;
        for (int x : sir) {
            suma += x;
        }
        double media = suma / n;

        System.out.println("Media aritmetica este: " + media);

        //scanner.close();
    }
}
