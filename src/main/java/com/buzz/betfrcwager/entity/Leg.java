package com.buzz.betfrcwager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Leg {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JsonIgnore
    @ManyToOne
    private Bet parent;

    private String propId;
    private String propValue;

    @Enumerated(EnumType.STRING)
    private BetStatus status;

    public Leg() { }

    public Leg(Bet parent, String propId, String propValue) {
        this.parent = parent;
        this.propId = propId;
        this.propValue = propValue;
        this.status = BetStatus.OPEN;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bet getParent() {
        return parent;
    }

    public void setParent(Bet parent) {
        this.parent = parent;
    }

    public String getPropId() {
        return propId;
    }

    public void setPropId(String propId) {
        this.propId = propId;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public BetStatus getStatus() {
        return status;
    }

    public void setStatus(BetStatus status) {
        this.status = status;
    }
}
