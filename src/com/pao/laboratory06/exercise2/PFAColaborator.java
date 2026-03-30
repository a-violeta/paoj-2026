package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends PersoanaFizica {

    private double cheltuieliLunare;

    public PFAColaborator(){}

    public PFAColaborator(String nume, String prenume, double venit_brut_lunar, double cheltuileiLunare) {
        super(nume, prenume, venit_brut_lunar);
        this.cheltuieliLunare=cheltuileiLunare;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        //PFA: (toate sumele sunt anuale):
        //venit net = (venit lunar - cheltuieli lunare) × 12
        //impozit pe venit: 10% × venit net
        //CASS (sănătate, 10%):
        //dacă venit net < 6 salarii minime brute/an: 10% × (6 × salariu minim brut)
        //dacă venit net între 6 și 72 salarii minime brute/an: 10% × venit net
        //dacă venit net > 72 salarii minime brute/an: 10% × (72 × salariu minim brut)
        //CAS (pensie, 25%):
        //dacă venit net < 12 salarii minime brute/an: nu se plătește
        //dacă venit net între 12 și 24 salarii minime brute/an: 25% × (12 × salariu minim brut)
        //dacă venit net > 24 salarii minime brute/an: 25% × (24 × salariu minim brut)
        //venit net anual = venit net - impozit - CASS - CAS
        return getVenitBrutLunar() * 12;
    }

    //@Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        venitBrutLunar = in.nextDouble();
        cheltuieliLunare = in.nextDouble();
    }

    //@Override
    public void afiseaza() {
        System.out.printf("%s %s %.1f %.1f%n", nume, prenume, venitBrutLunar, cheltuieliLunare);
    }

    //@Override
    public String tipContract() {
        return "PFA";
    }
}
