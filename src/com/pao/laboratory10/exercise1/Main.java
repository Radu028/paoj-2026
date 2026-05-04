package com.pao.laboratory10.exercise1;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // TODO: Implementează conform Readme.md
        //
        // Folosește LinkedList<Tranzactie> ca structură internă.
        // Citește comenzi din stdin până la EOF:
        //
        //   ENQUEUE id suma data tip   → addLast  (niciun output)
        //   DEQUEUE                    → removeFirst sau "Coada goala."
        //                                format: "Procesat: [id] data tip: suma RON"
        //   PUSH id suma data tip      → addFirst  (niciun output)
        //   POP                        → removeFirst sau "Coada goala."
        //                                format: "Extras: [id] data tip: suma RON"
        //   REMOVE_DEBIT               → Iterator.remove() pe toate DEBIT
        //                                afișează "Eliminat N tranzactii DEBIT."
        //   REMOVE_BELOW threshold     → Iterator.remove() pe suma < threshold
        //                                afișează "Eliminat N tranzactii sub threshold RON."
        //   PRINT                      → afișează toate, câte una pe linie
        //   SIZE                       → "Dimensiune coada: N"
        //
        // Format linie tranzacție: [id] data tip: suma RON
        //   Ex: [1] 2024-01-10 CREDIT: 500.00 RON

        LinkedList<Tranzactie> coada = new LinkedList<>();
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String linie = scanner.nextLine().trim();
            if (linie.isEmpty()) continue;

            String[] parts = linie.split("\\s+");
            String cmd = parts[0];

            switch (cmd) {
                case "ENQUEUE": {
                    Tranzactie t = parseTranzactie(parts);
                    coada.addLast(t);
                    break;
                }
                case "PUSH": {
                    Tranzactie t = parseTranzactie(parts);
                    coada.addFirst(t);
                    break;
                }
                case "DEQUEUE": {
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        Tranzactie t = coada.removeFirst();
                        System.out.println("Procesat: " + t);
                    }
                    break;
                }
                case "POP": {
                    if (coada.isEmpty()) {
                        System.out.println("Coada goala.");
                    } else {
                        Tranzactie t = coada.removeFirst();
                        System.out.println("Extras: " + t);
                    }
                    break;
                }
                case "REMOVE_DEBIT": {
                    int eliminate = 0;
                    Iterator<Tranzactie> itr = coada.iterator();
                    while (itr.hasNext()) {
                        Tranzactie t = itr.next();
                        if (t.getTip() == TipTranzactie.DEBIT) {
                            itr.remove();
                            eliminate++;
                        }
                    }
                    System.out.println("Eliminat " + eliminate + " tranzactii DEBIT.");
                    break;
                }
                case "REMOVE_BELOW": {
                    double threshold = Double.parseDouble(parts[1]);
                    int eliminate = 0;
                    Iterator<Tranzactie> itr = coada.iterator();
                    while (itr.hasNext()) {
                        Tranzactie t = itr.next();
                        if (t.getSuma() < threshold) {
                            itr.remove();
                            eliminate++;
                        }
                    }
                    System.out.printf("Eliminat %d tranzactii sub %.2f RON.%n", eliminate, threshold);
                    break;
                }
                case "PRINT": {
                    for (Tranzactie t : coada) {
                        System.out.println(t);
                    }
                    break;
                }
                case "SIZE": {
                    System.out.println("Dimensiune coada: " + coada.size());
                    break;
                }
            }
        }
    }

    private static Tranzactie parseTranzactie(String[] parts) {
        int id = Integer.parseInt(parts[1]);
        double suma = Double.parseDouble(parts[2]);
        String data = parts[3];
        TipTranzactie tip = TipTranzactie.valueOf(parts[4]);
        return new Tranzactie(id, suma, data, tip);
    }
}
