package com.pao.laboratory07.exercise3;

import com.pao.laboratory07.exercise1.OrderState;
import com.pao.laboratory07.exercise2.Comanda;
import com.pao.laboratory07.exercise2.ComandaStandard;
import com.pao.laboratory07.exercise2.ComandaRedusa;
import com.pao.laboratory07.exercise2.ComandaGratuita;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String line = sc.nextLine().trim();
            String[] tokens = line.split("\\s+");
            Comanda c = switch (tokens[0]) {
                case "STANDARD" -> {
                    var cmd = new ComandaStandard(tokens[1], Double.parseDouble(tokens[2]));
                    cmd.setClient(tokens[3]);
                    yield cmd;
                }
                case "DISCOUNTED" -> {
                    var cmd = new ComandaRedusa(tokens[1], Double.parseDouble(tokens[2]),
                            Integer.parseInt(tokens[3]));
                    cmd.setClient(tokens[4]);
                    yield cmd;
                }
                case "GIFT" -> {
                    var cmd = new ComandaGratuita(tokens[1]);
                    cmd.setClient(tokens[2]);
                    yield cmd;
                }
                default -> throw new IllegalArgumentException("Tip necunoscut: " + tokens[0]);
            };
            comenzi.add(c);
        }

        for (Comanda c : comenzi) {
            String base = c.descriere().replace("]", "] - client: " + c.getClient());
            System.out.println(base);
        }

        while (sc.hasNextLine()) {
            String cmd = sc.nextLine().trim();
            if (cmd.isEmpty()) continue;
            if (cmd.equals("QUIT")) break;

            if (cmd.equals("STATS")) {
                System.out.println();
                System.out.println("--- STATS ---");
                Map<String, DoubleSummaryStatistics> stats = comenzi.stream()
                        .collect(Collectors.groupingBy(
                                c -> tipLabel(c),
                                LinkedHashMap::new,
                                Collectors.summarizingDouble(Comanda::pretFinal)));
                for (var entry : stats.entrySet()) {
                    System.out.printf("%s: medie = %.2f lei%n",
                            entry.getKey(), entry.getValue().getAverage());
                }
            } else if (cmd.startsWith("FILTER")) {
                double threshold = Double.parseDouble(cmd.split("\\s+")[1]);
                System.out.println();
                System.out.printf("--- FILTER (>= %.2f) ---%n", threshold);
                comenzi.stream()
                        .filter(c -> c.pretFinal() >= threshold)
                        .forEach(c -> System.out.printf("%s: %s, pret: %.2f lei - client: %s%n",
                                tipLabel(c), c.getNume(), c.pretFinal(), c.getClient()));
            } else if (cmd.equals("SORT")) {
                System.out.println();
                System.out.println("--- SORT (by client, then by pret) ---");
                comenzi.stream()
                        .sorted(Comparator.comparing(Comanda::getClient)
                                .thenComparingDouble(Comanda::pretFinal))
                        .forEach(c -> {
                            if (c instanceof ComandaGratuita) {
                                System.out.printf("GIFT: %s, gratuit - client: %s%n",
                                        c.getNume(), c.getClient());
                            } else {
                                System.out.printf("%s: %s, pret: %.2f lei - client: %s%n",
                                        tipLabel(c), c.getNume(), c.pretFinal(), c.getClient());
                            }
                        });
            } else if (cmd.equals("SPECIAL")) {
                System.out.println();
                System.out.println("--- SPECIAL (discount > 15%) ---");
                comenzi.stream()
                        .filter(c -> c instanceof ComandaRedusa cr && cr.getDiscountProcent() > 15)
                        .forEach(c -> {
                            ComandaRedusa cr = (ComandaRedusa) c;
                            System.out.printf("DISCOUNTED: %s, pret: %.2f lei (-%d%%) - client: %s%n",
                                    c.getNume(), c.pretFinal(), cr.getDiscountProcent(), c.getClient());
                        });
            }
        }
    }

    private static String tipLabel(Comanda c) {
        return switch (c) {
            case ComandaStandard s -> "STANDARD";
            case ComandaRedusa r -> "DISCOUNTED";
            case ComandaGratuita g -> "GIFT";
        };
    }
}
