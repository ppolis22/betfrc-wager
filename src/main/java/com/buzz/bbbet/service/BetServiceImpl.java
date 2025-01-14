package com.buzz.bbbet.service;

import com.buzz.bbbet.entity.Bet;
import com.buzz.bbbet.entity.BetSlipPick;
import com.buzz.bbbet.entity.Leg;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BetServiceImpl implements BetService {
    @Override
    public void savePickToBetSlip(String userId, String propId, String propValue, String propOdds) {

    }

    @Override
    public List<BetSlipPick> getBetSlipPicks(String userId) {
        return null;
    }

    @Override
    public void clearBetSlip(String userId) {

    }

    @Override
    public Bet saveBet(Bet bet) {
        return null;
    }

    @Override
    public Leg saveLeg(Leg leg) {
        return null;
    }
}
