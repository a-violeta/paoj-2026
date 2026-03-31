package com.pao.laboratory06.exercise3;

public class Inginer extends Angajat implements PlataOnline, Comparable<Inginer> {

    private double sold = 0;

    public Inginer(String nume, String prenume, String telefon, double salariu) {
        super(nume, prenume, telefon, salariu);
        this.sold = salariu * 2; // exemplu: are un sold initial
    }

    @Override
    public void autentificare(String user, String parola) {
        if (user == null || parola == null || user.isEmpty() || parola.isEmpty()) {
            throw new IllegalArgumentException("User/parola invalide");
        }
        System.out.println("Inginer autentificat: " + user);
    }

    @Override
    public double consultareSold() {
        return sold;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (suma <= 0) return false;
        if (suma > sold) return false;
        sold -= suma;
        return true;
    }

    @Override
    public int compareTo(Inginer o) {
        return this.nume.compareTo(o.nume);
    }

    @Override
    public String toString() {
        return "Inginer " + nume + " " + prenume + " (salariu: " + salariu + ")";
    }
}
