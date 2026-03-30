package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class SRLColaborator extends PersoanaJuridica {

    private double cheltuieliLunare;

    public SRLColaborator(String nume, String prenume, double venit_brut_lunar, double cheltuieliLunare) {
        super(nume, prenume, venit_brut_lunar);
        this.cheltuieliLunare=cheltuieliLunare;
    }

    @Override
    public double calculeazaVenitNetAnual() {
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
        return "SRL";
    }
}
