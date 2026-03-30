package com.pao.laboratory06.exercise2;

public abstract class Colaborator {
    protected String nume;
    protected String prenume;
    protected double venitBrutLunar;

    public Colaborator(String nume, String prenume, double venit_brut_lunar){
        this.nume=nume;
        this.prenume=prenume;
        this.venitBrutLunar=venit_brut_lunar;
    }

    public double getVenitBrutLunar(){
        return this.venitBrutLunar;
    }

    public String getNume(){
        return this.nume;
    }

    public String getPrenume(){
        return this.prenume;
    }

    public abstract double calculeazaVenitNetAnual();
    //Creează subclase pentru fiecare tip: CIMColaborator, PFAColaborator, SRLColaborator
}
