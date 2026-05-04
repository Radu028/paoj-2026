package com.pao.laboratory11.exercise1;

import java.util.*;
import java.util.function.Predicate;

public class Main {
    private static final double FLAG_AMOUNT_THRESHOLD = 5000.00;
    private static final Set<String> RISKY_COUNTRIES = Set.of("NG", "RU", "UA", "CN", "BR", "KP");
    private static final Set<String> SUSPICIOUS_CHANNELS = Set.of("WEB", "MOBILE", "CRYPTO");

    public static void main(String[] args) {
        // Pipeline antifrauda compus din predicate:
        Predicate<Transaction> amountFlag = tx -> tx.getAmount() >= FLAG_AMOUNT_THRESHOLD;
        Predicate<Transaction> cryptoFlag = tx -> "CRYPTO".equals(tx.getChannel());
        Predicate<Transaction> flagRule = amountFlag.or(cryptoFlag);

        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine().trim());

        List<Transaction> all = new ArrayList<>();
        Map<Integer, Transaction> byId = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String[] parts = scanner.nextLine().trim().split("\\s+");
            int id = Integer.parseInt(parts[0]);
            double amount = Double.parseDouble(parts[1]);
            String date = parts[2];
            String country = parts[3];
            String channel = parts[4];

            int amountScore = Math.min(50, ((int) (amount / 1000)) * 10);
            int countryScore = RISKY_COUNTRIES.contains(country) ? 10 : 0;
            int channelScore;
            switch (channel) {
                case "WEB":
                case "APP":
                case "CRYPTO":
                case "MOBILE":
                    channelScore = 45;
                    break;
                case "POS":
                    channelScore = 25;
                    break;
                case "ATM":
                    channelScore = 20;
                    break;
                default:
                    channelScore = 0;
            }

            Transaction tmp = new Transaction(id, amount, date, country, channel, 0, false);
            boolean flagged = flagRule.test(tmp);
            int bonus = (flagged && SUSPICIOUS_CHANNELS.contains(channel)) ? 5 : 0;
            int score = amountScore + countryScore + channelScore + bonus;

            Transaction tx = new Transaction(id, amount, date, country, channel, score, flagged);
            all.add(tx);
            byId.put(id, tx);
        }

        Comparator<Transaction> cmp = Comparator
                .comparingInt(Transaction::getScore).reversed()
                .thenComparing(Comparator.comparingDouble(Transaction::getAmount).reversed())
                .thenComparing(Transaction::getDate)
                .thenComparingInt(Transaction::getId);

        int q = Integer.parseInt(scanner.nextLine().trim());
        for (int i = 0; i < q; i++) {
            String linie = scanner.nextLine().trim();
            if (linie.isEmpty()) continue;
            String[] parts = linie.split("\\s+");
            String cmd = parts[0];

            if (cmd.equals("CHECK")) {
                int id = Integer.parseInt(parts[1]);
                Transaction tx = byId.get(id);
                if (tx == null) {
                    System.out.println("CHECK " + id + " => NOT_FOUND");
                } else {
                    System.out.println("CHECK " + id + " => " + tx.getVerdict() + " score=" + tx.getScore());
                }
            } else if (cmd.equals("LIST_FLAGGED")) {
                List<Transaction> flagged = new ArrayList<>();
                for (Transaction tx : all) if (tx.isFlagged()) flagged.add(tx);
                if (flagged.isEmpty()) {
                    System.out.println("NONE");
                } else {
                    flagged.sort(cmp);
                    for (Transaction tx : flagged) {
                        System.out.println("[" + tx.getId() + "] FLAG score=" + tx.getScore());
                    }
                }
            } else if (cmd.equals("TOP_RISK")) {
                int k = Integer.parseInt(parts[1]);
                List<Transaction> sorted = new ArrayList<>(all);
                sorted.sort(cmp);
                int limit = Math.min(k, sorted.size());
                for (int j = 0; j < limit; j++) {
                    Transaction tx = sorted.get(j);
                    System.out.println("[" + tx.getId() + "] " + tx.getVerdict() + " score=" + tx.getScore());
                }
            } else {
                System.out.println("ERR UNKNOWN_COMMAND");
            }
        }
    }
}
