package com.buzz.bbbet.dto;

public class BetSlipPostResponseDto {
    private String parlayOdds;

    public BetSlipPostResponseDto(String parlayOdds) {
        this.parlayOdds = parlayOdds;
    }

    public String getParlayOdds() {
        return parlayOdds;
    }

    public void setParlayOdds(String parlayOdds) {
        this.parlayOdds = parlayOdds;
    }
}
