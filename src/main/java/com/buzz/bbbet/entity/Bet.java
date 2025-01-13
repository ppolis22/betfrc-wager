package com.buzz.bbbet.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany(mappedBy = "parent")
    private Set<Leg> legs;

    @ManyToOne
    private BetType type;

    private String userId;

    private double amount;  // better type for money?

    public Bet() { }

    public Bet(String id, Set<Leg> legs, BetType type, String userId, double amount) {
        this.id = id;
        this.legs = legs;
        this.type = type;
        this.userId = userId;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Leg> getLegs() {
        return legs;
    }

    public void setLegs(Set<Leg> legs) {
        this.legs = legs;
    }

    public BetType getType() {
        return type;
    }

    public void setType(BetType type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
