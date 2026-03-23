package com.pao.laboratory05.biblioteca;

import com.pao.laboratory05.playlist.Song;

public class Carte implements Comparable<Carte>{
    private String titlu;
    private String autor;
    private int an;
    private double rating;

    Carte(String titlu, String autor, int an, double rating){
        this.titlu=titlu;
        this.autor=autor;
        this.an=an;
        this.rating=rating;
    }

    String getTitlu(){
        return this.titlu;
    }

    String getAutor(){
        return this.autor;
    }

    int getAn(){
        return this.an;
    }

    double getRating(){
        return this.rating;
    }

    @Override
    public int compareTo(Carte o){
        //implements Comparable<Carte> — sortare după rating descrescător (cel mai bine cotat apare primul)
        return Double.compare(o.rating, this.rating);
    }

    @Override
    public String toString() {
        return "Carte{titlu='" + this.getTitlu() + "', autor='" + this.getAutor() + "', an=" + this.getAn() + ", rating=" + this.getRating() + "}";
    }

}
