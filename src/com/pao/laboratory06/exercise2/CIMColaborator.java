package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends PersoanaFizica{

    private boolean bonus=false;

    public CIMColaborator(){}

    public CIMColaborator(String nume, String prenume, double venit_brut_lunar, boolean bonus) {
        super(nume, prenume, venit_brut_lunar);
        this.bonus=bonus;
    }

    public boolean getBonus(){
        return bonus;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venit = getVenitBrutLunar() * 12 * 0.55;
        if (getBonus())
            venit = venit * 1.1;
        return venit;
    }

    //@Override
    public void citeste(Scanner in) {
        nume = in.next();
        prenume = in.next();
        venitBrutLunar = in.nextDouble();
        bonus = in.nextBoolean();
    }

    //@Override
    public void afiseaza() {
        System.out.printf("%s %s %.1f %b%n", nume, prenume, venitBrutLunar, bonus);
    }

    //@Override
    public String tipContract() {
        return "CIM";
    }

    //@Override
    public boolean areBonus() {
        return bonus;
    }
}
