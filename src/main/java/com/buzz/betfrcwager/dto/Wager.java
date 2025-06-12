package com.buzz.betfrcwager.dto;

import java.util.List;

public class Wager {
    private double amount;
    private List<WagerLeg> legs;

    public Wager() { }

    public Wager(double amount, List<WagerLeg> legs) {
        this.amount = amount;
        this.legs = legs;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<WagerLeg> getLegs() {
        return legs;
    }

    public void setLegs(List<WagerLeg> legs) {
        this.legs = legs;
    }
}
