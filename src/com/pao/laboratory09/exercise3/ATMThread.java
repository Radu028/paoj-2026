package com.pao.laboratory09.exercise3;

import java.util.Random;

public class ATMThread extends Thread {
    private final int atmId;
    private final CoadaTranzactii coada;
    private final int numarTranzactii;
    private final Random random;

    public ATMThread(int atmId, CoadaTranzactii coada, int numarTranzactii) {
        super("ATM-" + atmId);
        this.atmId = atmId;
        this.coada = coada;
        this.numarTranzactii = numarTranzactii;
        this.random = new Random(atmId * 1000L);
    }

    @Override
    public void run() {
        for (int i = 0; i < numarTranzactii; i++) {
            int id = atmId * 100 + i + 1;
            double suma = 50 + random.nextInt(950);
            Tranzactie t = new Tranzactie(id, suma, "2024-01-15");
            try {
                coada.adauga(t, atmId);
                System.out.printf("[ATM-%d] trimite: Tranzactie #%d %.2f RON%n", atmId, id, suma);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
