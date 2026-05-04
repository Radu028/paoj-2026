package com.pao.laboratory11.exercise3;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Vezi Readme.md pentru cerinte
        List<TransactionDemo> data = List.of(
                new TransactionDemo(1, 1500.00, "2026-05-01", "RO", "WEB"),
                new TransactionDemo(2, 750.50, "2026-05-02", "RU", "ATM"),
                new TransactionDemo(3, 200.00, "2026-05-03", "RO", "APP"),
                new TransactionDemo(4, 5200.00, "2026-05-04", "NG", "CRYPTO"),
                new TransactionDemo(5, 90.00, "2026-05-05", "RO", "POS"),
                new TransactionDemo(6, 3000.00, "2026-06-01", "RO", "WEB"),
                new TransactionDemo(7, 100.00, "2026-06-02", "RU", "WEB"),
                new TransactionDemo(8, 1200.00, "2026-06-03", "KP", "CRYPTO")
        );

        Snapshot snap = data.stream().collect(CustomCollectors.toSnapshot(3));

        System.out.println("--- 1. Top 3 tranzactii (din snapshot) ---");
        snap.getTopTransactions().forEach(System.out::println);

        System.out.println();
        System.out.println("--- 2. Numar tranzactii pe tara (desc) ---");
        snap.getCountByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.println(e.getKey() + " " + e.getValue()));

        System.out.println();
        System.out.println("--- 3. Numar tranzactii pe canal (desc) ---");
        snap.getCountByChannel().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.println(e.getKey() + " " + e.getValue()));

        System.out.println();
        System.out.printf("--- 4. Total agregat: %.2f RON ---%n", snap.getTotalAmount());

        System.out.println();
        System.out.println("--- 5. Snapshot imutabil: incercam modificare ---");
        try {
            snap.getCountByCountry().put("ZZ", 99L);
            System.out.println("EROARE: snapshot a fost modificat!");
        } catch (UnsupportedOperationException e) {
            System.out.println("OK: snapshot este imutabil (UnsupportedOperationException prins)");
        }
    }
}
