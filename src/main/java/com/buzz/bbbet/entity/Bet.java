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
    private Double wager;   // better type for money?
    private Integer odds;   // lives in Bet rather than Leg because of parlays

    public Bet() { }

    public Bet(String id, Set<Leg> legs, String type, String userId, Double wager, Integer odds) {
        this.id = id;
        this.legs = legs;
        this.type = type;
        this.userId = userId;
        this.wager = wager;
        this.odds = odds;
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

    public Double getWager() {
        return wager;
    }

    public void setWager(Double wager) {
        this.wager = wager;
    }

    public Integer getOdds() {
        return odds;
    }

    public void setOdds(Integer odds) {
        this.odds = odds;
    }
}
