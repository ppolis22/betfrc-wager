package com.buzz.bbbet.dto;

public class BetSlipPostResponseDto {
    private Integer parlayOdds;

    public BetSlipPostResponseDto(Integer parlayOdds) {
        this.parlayOdds = parlayOdds;
    }

    public Integer getParlayOdds() {
        return parlayOdds;
    }

    public void setParlayOdds(Integer parlayOdds) {
        this.parlayOdds = parlayOdds;
    }
}
