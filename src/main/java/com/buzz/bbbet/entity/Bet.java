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

    private String type;
    private String userId;
    private double wager;  // better type for money?

    public Bet() { }

    public Bet(String id, Set<Leg> legs, String type, String userId, double wager) {
        this.id = id;
        this.legs = legs;
        this.type = type;
        this.userId = userId;
        this.wager = wager;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getWager() {
        return wager;
    }

    public void setWager(double wager) {
        this.wager = wager;
    }
}
