package com.pao.laboratory11.exercise3;

import java.util.*;
import java.util.stream.Collector;

public final class CustomCollectors {
    private CustomCollectors() {}

    public static Collector<TransactionDemo, ?, Snapshot> toSnapshot(int topN) {
        class Agg {
            final Map<String, Long> byCountry = new HashMap<>();
            final Map<String, Long> byChannel = new HashMap<>();
            double total = 0.0;
            final List<TransactionDemo> all = new ArrayList<>();
        }
        return Collector.of(
                Agg::new,
                (agg, tx) -> {
                    agg.byCountry.merge(tx.getCountry(), 1L, Long::sum);
                    agg.byChannel.merge(tx.getChannel(), 1L, Long::sum);
                    agg.total += tx.getAmount();
                    agg.all.add(tx);
                },
                (a, b) -> {
                    b.byCountry.forEach((k, v) -> a.byCountry.merge(k, v, Long::sum));
                    b.byChannel.forEach((k, v) -> a.byChannel.merge(k, v, Long::sum));
                    a.total += b.total;
                    a.all.addAll(b.all);
                    return a;
                },
                agg -> {
                    Comparator<TransactionDemo> byAmountDesc =
                            Comparator.comparingDouble(TransactionDemo::getAmount).reversed()
                                    .thenComparingInt(TransactionDemo::getId);
                    List<TransactionDemo> top = new ArrayList<>(agg.all);
                    top.sort(byAmountDesc);
                    if (top.size() > topN) top = top.subList(0, topN);
                    return new Snapshot(agg.byCountry, agg.byChannel, agg.total, top);
                }
        );
    }
}
