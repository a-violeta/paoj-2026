package com.pao.laboratory05.biblioteca;

import com.pao.laboratory05.playlist.Song;

import java.util.Arrays;
import java.util.Comparator;

public class BibliotecaService {

    private Carte[] carti;

    private BibliotecaService(){
        this.carti=new Carte[0];
    }

    private static class Holder {
        private static final com.pao.laboratory05.biblioteca.BibliotecaService INSTANCE = new com.pao.laboratory05.biblioteca.BibliotecaService();
    }

    // Punct unic de acces la instanță
    public static com.pao.laboratory05.biblioteca.BibliotecaService getInstance() {
        return BibliotecaService.Holder.INSTANCE;
    }

    void addCarte(Carte carte){
        Carte[] tmp = new Carte[carti.length + 1];
        System.arraycopy(carti, 0, tmp, 0, carti.length);
        tmp[tmp.length - 1] = carte;
        carti=tmp;
        System.out.println("Carte adaugata: " + carte.getTitlu());
    }

    void listSortedByRating() {
        //clonează, Arrays.sort(copy) (natural = Comparable), afișează
        Carte[] copy = carti.clone();
        Arrays.sort(copy);

        for(Carte s : copy){
            System.out.println(s);
        }
    }

    void listSortedBy(Comparator<Carte> comparator) {
        //clonează, Arrays.sort(copy, comparator), afișează
        Carte[] copy=carti.clone();
        Arrays.sort(copy, comparator);

        for(Carte s : copy){
            System.out.println(s);
        }
    }
}
