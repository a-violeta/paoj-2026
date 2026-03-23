package com.pao.laboratory05.playlist;

import java.util.Arrays;

public class Playlist {

    private String name;
    private Song[] songs = new Song[0];

    Playlist(String name){
        this.name=name;
    }

    String getName(){
        return this.name;
    }

    void addSong(Song song){
        Song[] tmp = new Song[songs.length + 1];
        System.arraycopy(songs, 0, tmp, 0, songs.length);
        tmp[tmp.length - 1] = song;
        songs=tmp;
    }

    void printSortedByTitle(){
        //clonează array-ul, Arrays.sort(copy), afișează
        Song[] copy=songs.clone();
        Arrays.sort(copy);

        for(Song s : copy){
            System.out.println(s);
        }
    }

    void printSortedByDuration() {
        //Arrays.sort(copy, new SongDurationComparator()), afișează
        Song[] copy = songs.clone();
        Arrays.sort(copy, new SongDurationComparator());

        for(Song s : copy){
            System.out.println(s);
        }
    }

    int getTotalDuration() {
        //suma durationSeconds din toate song-urile
        int sum=0;
        for(Song s : songs){
            sum+=s.durationSeconds();
        }
        return sum;
    }
}
