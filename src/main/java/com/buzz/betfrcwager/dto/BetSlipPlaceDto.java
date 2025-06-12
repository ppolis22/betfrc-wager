package com.buzz.betfrcwager.dto;

import java.util.List;

public class BetSlipPlaceDto {
    private List<Wager> wagers;

    public BetSlipPlaceDto() { }

    public BetSlipPlaceDto(List<Wager> wagers) {
        this.wagers = wagers;
    }

    public List<Wager> getWagers() {
        return wagers;
    }

    public void setWagers(List<Wager> wagers) {
        this.wagers = wagers;
    }
}
