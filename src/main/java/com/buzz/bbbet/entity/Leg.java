package com.buzz.bbbet.entity;

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

    // foreign key into events db prop database
    private String propId;
    private String propValue;

    public Leg() { }

    public Leg(String id, Bet parent, String propId, String propValue) {
        this.id = id;
        this.parent = parent;
        this.propId = propId;
        this.propValue = propValue;
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
}
