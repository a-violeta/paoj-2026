package com.pao.laboratory09.exercise3;

public class ATMThread extends Thread {
    private final int atmId;
    private final CoadaTranzactii coada;

    public ATMThread(int atmId, CoadaTranzactii coada) {
        this.atmId = atmId;
        this.coada = coada;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 4; i++) {
                Tranzactie t = new Tranzactie(
                        atmId * 100 + i,
                        100 + i * 10,
                        "2026-01-15"
                );

                System.out.println("[ATM-" + atmId + "] trimite: Tranzactie #" + t.id + " " + t.suma + " RON");
                coada.adauga(t, atmId);

                Thread.sleep(50);
            }
        } catch (InterruptedException ignored) {}
    }
}
