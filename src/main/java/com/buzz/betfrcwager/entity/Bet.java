package com.buzz.betfrcwager.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Leg> legs;

    private String userId;
    private Double wager;   // better type for money?
    private Integer odds;   // lives in Bet rather than Leg because of parlays

    @Enumerated(EnumType.STRING)
    private BetStatus status;
    private LocalDateTime placedDate;
    private LocalDateTime settledDate;

    public Bet() { }

    public Bet(String userId, Double wager, Integer odds) {
        this.legs = new ArrayList<>();
        this.userId = userId;
        this.wager = wager;
        this.odds = odds;
        this.status = BetStatus.OPEN;
        this.placedDate = LocalDateTime.now();
        this.settledDate = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
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

    public BetStatus getStatus() {
        return status;
    }

    public void setStatus(BetStatus status) {
        this.status = status;
    }

    public LocalDateTime getPlacedDate() {
        return placedDate;
    }

    public void setPlacedDate(LocalDateTime placedDate) {
        this.placedDate = placedDate;
    }

    public LocalDateTime getSettledDate() {
        return settledDate;
    }

    public void setSettledDate(LocalDateTime settledDate) {
        this.settledDate = settledDate;
    }
}
