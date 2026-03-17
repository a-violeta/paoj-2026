package com.pao.laboratory01.comparators;

// EXERCITIU 1: cream in pachetul comparators o clasa Podcast cu durata (secunde, int) si titlu (string)
// dupa modelul AudioBook.java si Book.java, implementati:
// 1. toString — pentru afisare frumoasa
// 2. Comparable<Podcast> cu compareTo — sortare dupa titlu
// 3. un Comparator extern (PodcastLengthComparator) — sortare dupa durata
// 4. o metoda main in care cream cateva podcast-uri si le sortam in ambele moduri

import java.util.Comparator;

public class Podcast implements Comparable<Podcast> {

    private String title;
    private int durationInSeconds;

    public Podcast(String title, int duration){
        this.title=title;
        this.durationInSeconds=duration;
    }

    @Override
    public String toString(){
        return "Podcast{ title = " + title + ", durationInSeconds = " + durationInSeconds + " }";
    }

    @Override
    public int compareTo(Podcast o) {
        return this.title.compareTo(o.title);
    }

    public int getDurationInSeconds(){
        return durationInSeconds;
    }

    // Metoda main — codul final care trebuie sa functioneze dupa implementare.
    // Ruleaza-l ca sa verifici ca totul e corect!
    public static void main(String[] args) {
        // cream cateva podcast-uri
        Podcast[] podcasts = {
                new Podcast("Tech Talk", 2400),
                new Podcast("Arta Conversatiei", 3600),
                new Podcast("Mindset", 1800)
        };

        // 1. sortare naturala (compareTo) — dupa titlu
        java.util.Arrays.sort(podcasts);
        System.out.println("Sortate dupa titlu:");
        System.out.println(java.util.Arrays.toString(podcasts));

        // 2. sortare cu Comparator extern — dupa durata
        java.util.Arrays.sort(podcasts, new PodcastLengthComparator());
        System.out.println("Sortate dupa durata (crescator):");
        System.out.println(java.util.Arrays.toString(podcasts));

        // 3. sortare cu lambda — dupa durata descrescator
        java.util.Arrays.sort(podcasts,
                (p1, p2) -> p2.getDurationInSeconds() - p1.getDurationInSeconds()
        );
        System.out.println("Sortate dupa durata (descrescator, lambda):");
        System.out.println(java.util.Arrays.toString(podcasts));
    }
}

class PodcastLengthComparator implements Comparator<Podcast> {
    @Override
        public int compare(Podcast o1, Podcast o2) {
        return o1.getDurationInSeconds() - o2.getDurationInSeconds();
    }
}

