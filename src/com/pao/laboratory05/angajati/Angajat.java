package com.pao.laboratory05.angajati;

public class Angajat implements Comparable<Angajat>{
    private String nume;
    private Departament departament;
    private double salariu;

    public Angajat(String nume, Departament departament, double salariu){
        this.nume=nume;
        this.departament=departament;
        this.salariu=salariu;
    }

    public String getNume(){
        return this.nume;
    }

    public Departament getDepartament(){
        return this.departament;
    }

    public double getSalariu(){
        return this.salariu;
    }

    @Override
    public String toString(){
        return "Angajat{nume='...', departament=Departament[nume=..., locatie=...], salariu=...}";
    }

    @Override
    public int compareTo(Angajat o) {
        //sortare după salariu descrescător (cel mai bine plătit apare primul)
        return Double.compare(o.salariu, this.salariu);
    }
}
