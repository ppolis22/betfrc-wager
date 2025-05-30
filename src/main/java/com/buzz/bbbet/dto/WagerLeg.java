package com.buzz.bbbet.dto;

import java.util.Objects;

public class WagerLeg {
    private String propId;
    private String value;
    private Integer odds;

    public WagerLeg() {}

    public WagerLeg(String propId, String value, Integer odds) {
        this.propId = propId;
        this.value = value;
        this.odds = odds;
    }

    public String getPropId() {
        return propId;
    }

    public void setPropId(String propId) {
        this.propId = propId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getOdds() {
        return odds;
    }

    public void setOdds(Integer odds) {
        this.odds = odds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WagerLeg wagerLeg = (WagerLeg) o;
        return propId.equals(wagerLeg.propId) && value.equals(wagerLeg.value) && odds.equals(wagerLeg.odds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propId, value, odds);
    }
}
