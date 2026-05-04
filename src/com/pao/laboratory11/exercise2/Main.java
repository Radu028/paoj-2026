package com.pao.laboratory11.exercise2;

import com.pao.laboratory11.exercise1.Transaction;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static class Entry {
        final int id;
        final double amount;
        final String date;
        final String country;
        final String channel;
        final String accountId;

        Entry(int id, double amount, String date, String country, String channel, String accountId) {
            this.id = id;
            this.amount = amount;
            this.date = date;
            this.country = country;
            this.channel = channel;
            this.accountId = accountId;
        }
    }

    public static void main(String[] args) {
        // Refoloseste com.pao.laboratory11.exercise1.Transaction conform cerintei.
        Transaction marker = new Transaction(0, 0.0, "", "", "", 0, false);
        marker.getId();

        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine().trim());

        List<Entry> data = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String[] parts = scanner.nextLine().trim().split("\\s+");
            int id = Integer.parseInt(parts[0]);
            double amount = Double.parseDouble(parts[1]);
            String date = parts[2];
            String country = parts[3];
            String channel = parts[4];
            String accountId = parts[5];
            data.add(new Entry(id, amount, date, country, channel, accountId));
        }

        int q = Integer.parseInt(scanner.nextLine().trim());
        for (int i = 0; i < q; i++) {
            String linie = scanner.nextLine().trim();
            if (linie.isEmpty()) continue;
            String[] parts = linie.split("\\s+");
            String cmd = parts[0];

            switch (cmd) {
                case "REPORT_MONTH": {
                    String month = parts[1];
                    List<Entry> filtered = data.stream()
                            .filter(e -> e.date.startsWith(month))
                            .collect(Collectors.toList());
                    double total = filtered.stream().mapToDouble(e -> e.amount).sum();
                    System.out.printf("MONTH %s total=%.2f count=%d%n", month, total, filtered.size());
                    break;
                }
                case "REPORT_ACCOUNT": {
                    String acc = parts[1];
                    List<Entry> filtered = data.stream()
                            .filter(e -> e.accountId.equals(acc))
                            .collect(Collectors.toList());
                    double total = filtered.stream().mapToDouble(e -> e.amount).sum();
                    System.out.printf("ACCOUNT %s total=%.2f count=%d%n", acc, total, filtered.size());
                    break;
                }
                case "TOP_CHANNELS": {
                    int k = Integer.parseInt(parts[1]);
                    Map<String, Long> counts = data.stream()
                            .collect(Collectors.groupingBy(e -> e.channel, Collectors.counting()));
                    counts.entrySet().stream()
                            .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                                    .thenComparing(Map.Entry.comparingByKey()))
                            .limit(k)
                            .forEach(e -> System.out.println(e.getKey() + " " + e.getValue()));
                    break;
                }
                default:
                    System.out.println("ERR UNKNOWN_COMMAND");
            }
        }
    }
}
