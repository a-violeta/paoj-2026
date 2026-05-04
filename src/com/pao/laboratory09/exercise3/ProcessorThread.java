package com.pao.laboratory09.exercise3;

public class ProcessorThread implements Runnable {

    public volatile boolean activ = true;
    private final CoadaTranzactii coada;

    public ProcessorThread(CoadaTranzactii coada) {
        this.coada = coada;
    }

    @Override
    public void run() {
        try {
            while (true) {

                // conditia de oprire: activ == false si coada goala
                synchronized (coada) {
                    if (!activ && coada.isEmpty()) {
                        break;
                    }
                }

                Tranzactie t = coada.extrage();
                System.out.println("[Processor] Factura #" + t.id + " - " + t.suma + " RON | " + t.data);

                Thread.sleep(80);
            }
        } catch (InterruptedException ignored) {}
    }
}
