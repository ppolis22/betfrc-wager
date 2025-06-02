package com.buzz.bbbet.service;

import com.buzz.bbbet.dto.BetSlipPlaceDto;
import com.buzz.bbbet.entity.Bet;
import com.buzz.bbbet.entity.Leg;
import com.buzz.bbbet.exception.InvalidRequestException;

import java.util.List;

public interface BetService {
    List<Bet> handleBetslipPost(BetSlipPlaceDto betslip, String userId) throws InvalidRequestException;
    List<Bet> getBetsForUser(String userId);
}
