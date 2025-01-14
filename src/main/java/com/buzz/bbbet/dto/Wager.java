package com.buzz.bbbet.dto;

public class Wager {
    private static final String PARLAY_KEY = "parlay";

    private String propId;
    private double amount;

    public Wager() { }

    public Wager(String propId, double amount) {
        this.propId = propId;
        this.amount = amount;
    }

    public String getPropId() {
        return propId;
    }

    public void setPropId(String propId) {
        this.propId = propId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isParlayWager() {
        return this.propId.equalsIgnoreCase(PARLAY_KEY);
    }
}
