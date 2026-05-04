package com.pao.laboratory09.exercise3;

public class Main {
    public static void main(String[] args) {
        CoadaTranzactii coada = new CoadaTranzactii();

        ATMThread atm1 = new ATMThread(1, coada);
        ATMThread atm2 = new ATMThread(2, coada);
        ATMThread atm3 = new ATMThread(3, coada);

        ProcessorThread processor = new ProcessorThread(coada);
        Thread processorThread = new Thread(processor);

        // start producatori
        atm1.start();
        atm2.start();
        atm3.start();

        // start consumator
        processorThread.start();

        // asteapta ATM uri
        try {
            atm1.join();
            atm2.join();
            atm3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // opreste procesorul
        synchronized (coada) {
            processor.activ = false;
            coada.notifyAll(); // trezeste extrage() daca e blocant
        }

        try {
            processorThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Toate tranzactiile procesate. Total: 12");
    }
}
