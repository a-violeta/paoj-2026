package com.pao.laboratory05.biblioteca;

import java.util.Comparator;

public class CarteAnComparator implements Comparator<Carte> {
    //după an crescător (cea mai veche apare prima)
    @Override
    public int compare(Carte c1, Carte c2){
        return Integer.compare(c1.getAn(), c2.getAn());
    }
}
