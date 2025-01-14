package com.buzz.bbbet.service;

import com.buzz.bbbet.entity.Bet;
import com.buzz.bbbet.entity.BetSlipPick;
import com.buzz.bbbet.entity.Leg;

import java.util.List;

public interface BetService {
    void savePickToBetSlip(String userId, String propId, String propValue, String propOdds);
    List<BetSlipPick> getBetSlipPicks(String userId);
    void clearBetSlip(String userId);
    Bet saveBet(Bet bet);
    Leg saveLeg(Leg leg);
}
