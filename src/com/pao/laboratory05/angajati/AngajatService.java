package com.pao.laboratory05.angajati;


import com.pao.laboratory05.biblioteca.Carte;

import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati;

    private AngajatService(){
        this.angajati=new Angajat[0];
    }

    private static class Holder {
        private static final com.pao.laboratory05.angajati.AngajatService INSTANCE = new com.pao.laboratory05.angajati.AngajatService();
    }

    // Punct unic de acces la instanță
    public static com.pao.laboratory05.angajati.AngajatService getInstance() {
        return AngajatService.Holder.INSTANCE;
    }

    void addAngajat(Angajat a){
        Angajat[] tmp = new Angajat[angajati.length + 1];
        System.arraycopy(angajati, 0, tmp, 0, angajati.length);
        tmp[tmp.length - 1] = a;
        angajati=tmp;
        System.out.println("Angajat adaugat: " + a.getNume());
    }

    void printAll(){
        for(Angajat a:angajati){
            System.out.println(a);
        }
    }

    void listBySalary() {
        //clonează, Arrays.sort(copy), afișează (descrescător, natural)
        Angajat[] copy = angajati.clone();
        Arrays.sort(copy);

        for(Angajat s : copy){
            System.out.println(s);
        }
    }

    void findByDepartament(String numeDept){
        //parcurge array-ul, afișează toți angajații al căror angajat.getDepartament().nume().equalsIgnoreCase(numeDept);
        //dacă nu găsește niciun angajat, afișează "Niciun angajat în departamentul: <numeDept>"
    }
}
