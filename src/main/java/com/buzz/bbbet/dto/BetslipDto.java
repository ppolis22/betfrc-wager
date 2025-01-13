package com.buzz.bbbet.dto;

import com.buzz.bbbet.entity.Bet;

import java.util.List;

// This is tricky since wherever the betslip db lives it will need to share a schema
public class BetslipDto {
    private List<Bet> bets;

    public List<Bet> getBets() {
        return bets;
    }

    public void setBets(List<Bet> bets) {
        this.bets = bets;
    }
}
