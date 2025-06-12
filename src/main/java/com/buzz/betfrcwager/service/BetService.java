package com.buzz.betfrcwager.service;

import com.buzz.betfrcwager.dto.BetSlipPlaceDto;
import com.buzz.betfrcwager.entity.Bet;
import com.buzz.betfrcwager.exception.InvalidRequestException;
import com.buzz.betfrcwager.exception.ServiceConnectionException;

import java.util.List;

public interface BetService {
    List<Bet> handleBetslipPost(BetSlipPlaceDto betslip, String userId) throws InvalidRequestException, ServiceConnectionException;
    List<Bet> getBetsForUser(String userId);
}
