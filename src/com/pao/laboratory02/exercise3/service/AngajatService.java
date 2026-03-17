package com.pao.laboratory02.exercise3.service;

import com.pao.laboratory02.exercise3.model.Angajat;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Completează cele 3 metode.
 * Folosește ArrayList — nu mai e nevoie de redimensionare manuală.
 */
public class AngajatService {
    private List<Angajat> angajati;

    public AngajatService() {
        this.angajati = new ArrayList<>();
    }

    public void addAngajat(Angajat a) {
        angajati.add(a);
        System.out.println("Angajat adaugat: " + a.getName());
    }

    public void listAll() {
        if (angajati.isEmpty()){
            System.out.println("Nu sunt angajati");
        }
        else{
            int i;
            for(i=0; i<angajati.size(); i++){
                System.out.println((i+1) + ". " + angajati.get(i));
            }
        }
    }

    public double totalSalarii() {
        double s=0;
        for (Angajat angajat : angajati) {
            s += angajat.salariuTotal();
        }
        return s;
    }
}
