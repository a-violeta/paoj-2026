package com.pao.laboratory09.exercise1;

import java.io.Serializable;
import java.util.Locale;

public class Tranzactie implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private double suma;
    private String data;
    private String contSursa;
    private String contDestinatie;
    private TipTranzactie tip;

    transient String note;

    public Tranzactie(int id, double suma, String data,
                      String contSursa, String contDestinatie,
                      TipTranzactie tip) {
        this.id = id;
        this.suma = suma;
        this.data = data;
        this.contSursa = contSursa;
        this.contDestinatie = contDestinatie;
        this.tip = tip;
    }

    public int getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public TipTranzactie getTip() {
        return tip;
    }

    public double getSuma() {
        return suma;
    }

    public String getContSursa() {
        return contSursa;
    }

    public String getContDestinatie() {
        return contDestinatie;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "[%d] %s %s: %.2f RON | %s -> %s",
                id, data, tip, suma, contSursa, contDestinatie);
    }
}
