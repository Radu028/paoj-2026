package com.pao.laboratory09.exercise3;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Vezi Readme.md pentru cerințe
        CoadaTranzactii coada = new CoadaTranzactii();

        ATMThread atm1 = new ATMThread(1, coada, 4);
        ATMThread atm2 = new ATMThread(2, coada, 4);
        ATMThread atm3 = new ATMThread(3, coada, 4);

        ProcessorThread processor = new ProcessorThread(coada);
        Thread processorThread = new Thread(processor, "Processor");

        atm1.start();
        atm2.start();
        atm3.start();
        processorThread.start();

        atm1.join();
        atm2.join();
        atm3.join();

        processor.activ = false;
        coada.inchide();
        processorThread.join();

        System.out.println("Toate tranzactiile procesate. Total: " + processor.getProcesate());
    }
}
