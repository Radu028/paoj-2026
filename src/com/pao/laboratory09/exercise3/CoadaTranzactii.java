package com.pao.laboratory09.exercise3;

import java.util.ArrayDeque;
import java.util.Deque;

public class CoadaTranzactii {
    private static final int CAPACITATE_MAX = 5;
    private final Deque<Tranzactie> banda = new ArrayDeque<>();
    private volatile boolean inchisa = false;

    public synchronized void adauga(Tranzactie t, int atmId) throws InterruptedException {
        while (banda.size() >= CAPACITATE_MAX) {
            System.out.println("[ATM-" + atmId + "] astept loc...");
            wait();
        }
        banda.addLast(t);
        notifyAll();
    }

    public synchronized Tranzactie extrage() throws InterruptedException {
        while (banda.isEmpty() && !inchisa) {
            wait();
        }
        if (banda.isEmpty()) {
            return null;
        }
        Tranzactie t = banda.pollFirst();
        notifyAll();
        return t;
    }

    public synchronized void inchide() {
        inchisa = true;
        notifyAll();
    }

    public synchronized boolean esteGoala() {
        return banda.isEmpty();
    }
}
