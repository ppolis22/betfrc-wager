package com.buzz.bbbet.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BetSlipPickId implements Serializable {
    private String userId;  // FK into user table
    private String propId;  // FK into props table

    public BetSlipPickId() { }

    public BetSlipPickId(String userId, String propId) {
        this.userId = userId;
        this.propId = propId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BetSlipPickId that = (BetSlipPickId) o;
        return userId.equals(that.userId) && propId.equals(that.propId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, propId);
    }
}
