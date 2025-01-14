package com.buzz.bbbet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class BetSlipPick {

    @Id
    private String userId;  // FK into user table

    @Id
    private String propId;  // FK into props table

    // These are the latest prop value and odds acknowledged by the
    // user, must match current values at submit time.
    //
    // For now, only handle one prop value in the bet slip at a time
    // e.g. can't have Over 25.5 and Over 26.5 in the same bet slip
    // for the same prop. In the future, perhaps those can be considered
    // different props with different Ids and all.
    private String ackPropValue;
    private Integer ackOdds;

    public BetSlipPick() { }

    public BetSlipPick(String userId, String propId, String ackPropValue, Integer ackOdds) {
        this.userId = userId;
        this.propId = propId;
        this.ackPropValue = ackPropValue;
        this.ackOdds = ackOdds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPropId() {
        return propId;
    }

    public void setPropId(String propId) {
        this.propId = propId;
    }

    public String getAckPropValue() {
        return ackPropValue;
    }

    public void setAckPropValue(String ackPropValue) {
        this.ackPropValue = ackPropValue;
    }

    public Integer getAckOdds() {
        return ackOdds;
    }

    public void setAckOdds(Integer ackOdds) {
        this.ackOdds = ackOdds;
    }
}
