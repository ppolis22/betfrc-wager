package com.buzz.bbbet.service;

import com.buzz.bbbet.entity.Bet;
import com.buzz.bbbet.entity.BetSlipPick;
import com.buzz.bbbet.entity.BetSlipPickId;
import com.buzz.bbbet.entity.Leg;
import com.buzz.bbbet.repo.BetRepository;
import com.buzz.bbbet.repo.BetSlipPickRepository;
import com.buzz.bbbet.repo.LegRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BetServiceImpl implements BetService {

    private final BetSlipPickRepository betSlipPickRepository;
    private final BetRepository betRepository;
    private final LegRepository legRepository;

    public BetServiceImpl(
            BetSlipPickRepository betSlipPickRepository,
            BetRepository betRepository,
            LegRepository legRepository
    ) {
        this.betSlipPickRepository = betSlipPickRepository;
        this.betRepository = betRepository;
        this.legRepository = legRepository;
    }

    @Override
    public void savePickToBetSlip(String userId, String propId, String propValue, Integer propOdds) {
        BetSlipPickId pickId = new BetSlipPickId(userId, propId);
        BetSlipPick pick = new BetSlipPick(pickId, propValue, propOdds);
        betSlipPickRepository.save(pick);
    }

    @Override
    public List<BetSlipPick> getBetSlipPicks(String userId) {
        return betSlipPickRepository.findByIdUserId(userId);
    }

    @Override
    public void clearBetSlip(String userId) {

    }

    @Override
    public Bet saveBet(Bet bet) {
        return betRepository.save(bet);
    }

    @Override
    public Leg saveLeg(Leg leg) {
        return legRepository.save(leg);
    }
}
