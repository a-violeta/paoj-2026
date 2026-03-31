package com.pao.laboratory06.exercise2;

import java.util.Locale;
import java.util.Scanner;

public class CIMColaborator extends PersoanaFizica {

    private boolean bonus = false;

    @Override
    public void citeste(Scanner in) {
        // Main a citit deja "CIM"
        nume = in.next();
        prenume = in.next();
        venitBrutLunar = in.nextDouble();

        if (in.hasNext()) {
            String token = in.next();
            if (token.equalsIgnoreCase("DA")) bonus = true;
        }
    }

    @Override
    public void afiseaza() {
        System.out.printf(Locale.US,
                "CIM: %s %s, venit net anual: %.2f lei%n",
                nume, prenume, calculeazaVenitNetAnual());
    }

    @Override
    public TipColaborator getTip() {
        return TipColaborator.CIM;
    }

    @Override
    public boolean areBonus() {
        return bonus;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double net = venitBrutAnual() * 0.55;
        if (bonus) net *= 1.10;
        return net;
    }

    @Override
    public String tipContract() {
        return "CIM";
    }

}
