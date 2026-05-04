package com.pao.laboratory11.exercise3;

import java.util.*;

public final class Snapshot {
    private final Map<String, Long> countByCountry;
    private final Map<String, Long> countByChannel;
    private final double totalAmount;
    private final List<TransactionDemo> topTransactions;

    public Snapshot(Map<String, Long> byCountry, Map<String, Long> byChannel,
                    double total, List<TransactionDemo> top) {
        this.countByCountry = Collections.unmodifiableMap(new LinkedHashMap<>(byCountry));
        this.countByChannel = Collections.unmodifiableMap(new LinkedHashMap<>(byChannel));
        this.totalAmount = total;
        this.topTransactions = List.copyOf(top);
    }

    public Map<String, Long> getCountByCountry() { return countByCountry; }
    public Map<String, Long> getCountByChannel() { return countByChannel; }
    public double getTotalAmount() { return totalAmount; }
    public List<TransactionDemo> getTopTransactions() { return topTransactions; }
}
