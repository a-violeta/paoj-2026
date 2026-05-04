package com.pao.laboratory09.exercise3;

import java.util.LinkedList;
import java.util.Queue;

public class CoadaTranzactii {
    private final Queue<Tranzactie> coada = new LinkedList<>();
    private final int CAPACITATE = 5;

    public synchronized void adauga(Tranzactie t, int atmId) throws InterruptedException {
        while (coada.size() == CAPACITATE) {
            System.out.println("[ATM-" + atmId + "] astept loc...");
            wait();
        }
        coada.add(t);
        notifyAll();
    }

    public synchronized Tranzactie extrage() throws InterruptedException {
        while (coada.isEmpty()) {
            wait();
        }
        Tranzactie t = coada.poll();
        notifyAll();
        return t;
    }

    public synchronized boolean isEmpty() {
        return coada.isEmpty();
    }

}
