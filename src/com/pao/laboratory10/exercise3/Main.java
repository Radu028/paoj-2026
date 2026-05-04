package com.pao.laboratory10.exercise3;

import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        // Vezi Readme.md pentru cerințe
        List<Tranzactie> tranzactii = List.of(
                new Tranzactie(1, 1500.00, "2024-01-15", TipTranzactie.CREDIT, "RO01"),
                new Tranzactie(2, 750.50, "2024-01-22", TipTranzactie.DEBIT, "RO02"),
                new Tranzactie(3, 200.00, "2024-02-05", TipTranzactie.CREDIT, "RO01"),
                new Tranzactie(4, 1200.00, "2024-02-18", TipTranzactie.DEBIT, "RO03"),
                new Tranzactie(5, 500.00, "2024-03-10", TipTranzactie.CREDIT, "RO02"),
                new Tranzactie(6, 300.00, "2024-03-22", TipTranzactie.DEBIT, "RO01"),
                new Tranzactie(7, 980.00, "2024-01-30", TipTranzactie.CREDIT, "RO03"),
                new Tranzactie(8, 50.00, "2024-02-25", TipTranzactie.DEBIT, "RO02"),
                new Tranzactie(9, 2000.00, "2024-03-15", TipTranzactie.CREDIT, "RO01"),
                new Tranzactie(10, 400.00, "2024-03-28", TipTranzactie.DEBIT, "RO03")
        );

        System.out.println("--- 1. Tranzactii CREDIT ---");
        tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);

        System.out.println();
        double total = tranzactii.stream().mapToDouble(Tranzactie::getSuma).sum();
        System.out.printf("--- 2. Total procesat: %.2f RON ---%n", total);

        System.out.println();
        System.out.println("--- 3. Total per luna ---");
        Map<String, Double> totalPerLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.summingDouble(Tranzactie::getSuma)));
        totalPerLuna.forEach((luna, suma) ->
                System.out.printf("%s: %.2f RON%n", luna, suma));

        System.out.println();
        System.out.println("--- 4. Top 3 tranzactii ---");
        tranzactii.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3)
                .forEach(System.out::println);

        System.out.println();
        List<String> conturi = tranzactii.stream()
                .map(Tranzactie::getContSursa)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("--- 5. Conturi sursa unice: " + conturi + " ---");

        System.out.println();
        double medie = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .average()
                .orElse(0.0);
        System.out.printf("--- 6. Suma medie: %.2f RON ---%n", medie);

        System.out.println();
        System.out.println("--- 7. EXTRASE DE CONT ---");
        Map<String, List<Tranzactie>> peLuni = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.toList()));
        peLuni.forEach((luna, lst) -> {
            double s = lst.stream().mapToDouble(Tranzactie::getSuma).sum();
            System.out.printf("EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n",
                    luna, lst.size(), s);
        });
    }
}
