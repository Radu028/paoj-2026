package com.pao.laboratory09.exercise3;

public class ProcessorThread implements Runnable {
    public volatile boolean activ = true;
    private final CoadaTranzactii coada;
    private int procesate = 0;

    public ProcessorThread(CoadaTranzactii coada) {
        this.coada = coada;
    }

    public int getProcesate() {
        return procesate;
    }

    @Override
    public void run() {
        while (true) {
            Tranzactie t;
            try {
                t = coada.extrage();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            if (t == null) {
                if (!activ) return;
                continue;
            }
            System.out.printf("[Processor] Factura #%d - %.2f RON | %s%n",
                    t.getId(), t.getSuma(), t.getData());
            procesate++;
            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
