package com.pao.laboratory11.exercise1;

public class Transaction {
    private final int id;
    private final double amount;
    private final String date;
    private final String country;
    private final String channel;
    private final int score;
    private final boolean flagged;

    public Transaction(int id, double amount, String date, String country, String channel,
                       int score, boolean flagged) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.country = country;
        this.channel = channel;
        this.score = score;
        this.flagged = flagged;
    }

    public int getId() { return id; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public String getCountry() { return country; }
    public String getChannel() { return channel; }
    public int getScore() { return score; }
    public boolean isFlagged() { return flagged; }
    public String getVerdict() { return flagged ? "FLAG" : "ALLOW"; }
}
