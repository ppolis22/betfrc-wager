package com.buzz.bbbet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class BetType {
    @Id
    @Column(name = "NAME", nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "type")
    private List<Bet> betsOfType;

    public BetType() { }

    public BetType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Bet> getBetsOfType() {
        return betsOfType;
    }

    public void setBetsOfType(List<Bet> betsOfType) {
        this.betsOfType = betsOfType;
    }
}
