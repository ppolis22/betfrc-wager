package com.buzz.betfrcwager.service;

import com.buzz.betfrcwager.dto.BetSlipPlaceDto;
import com.buzz.betfrcwager.dto.Wager;
import com.buzz.betfrcwager.dto.WagerLeg;
import com.buzz.betfrcwager.entity.Bet;
import com.buzz.betfrcwager.entity.Leg;
import com.buzz.betfrcwager.exception.InvalidRequestException;
import com.buzz.betfrcwager.exception.ServiceConnectionException;
import com.buzz.betfrcwager.external.OddsRequestDto;
import com.buzz.betfrcwager.repo.BetRepository;
import com.buzz.betfrcwager.repo.LegRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class BetServiceImpl implements BetService {

    private final BetRepository betRepository;
    private final LegRepository legRepository;

    @Value("${buzz.app.odds-service}")
    private String oddsService;

    public BetServiceImpl(
            BetRepository betRepository,
            LegRepository legRepository
    ) {
        this.betRepository = betRepository;
        this.legRepository = legRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Bet> handleBetslipPost(BetSlipPlaceDto betslip, String userId) throws InvalidRequestException, ServiceConnectionException {
        // verify betslip values match current odds
        validateBetslip(betslip);

        // try to deduct funds from funds service
        Double betTotal = betslip.getWagers().stream().mapToDouble(Wager::getAmount).sum();
        boolean didDeductFunds = deductFunds(userId, betTotal);

        // write to bets db (or later, kafka queue), if fails refund funds
        List<Bet> createdBets = new ArrayList<>();
        for (Wager wager : betslip.getWagers()) {
            int odds = wager.getLegs().size() > 1 ? calculateParlayOdds(wager.getLegs()) : wager.getLegs().get(0).getOdds();
            Bet bet = new Bet(userId, wager.getAmount(), odds);
            for (WagerLeg wagerLeg : wager.getLegs()) {
                Leg leg = new Leg(bet, wagerLeg.getPropId(), wagerLeg.getValue());
                bet.getLegs().add(leg);
            }
            bet = betRepository.save(bet);
            createdBets.add(bet);
        }

        return createdBets;
    }

    @Override
    public List<Bet> getBetsForUser(String userId) {
        List<Bet> userBets = betRepository.findByUserId(userId);
        return userBets;
    }

    private void validateBetslip(BetSlipPlaceDto betslip) throws InvalidRequestException, ServiceConnectionException {
        Set<WagerLeg> validatedLegs = new HashSet<>();

        // verify all wagers are valid
        for (Wager wager : betslip.getWagers()) {
            if (wager.getLegs().size() == 0 || !canParlayLegs(wager.getLegs()) || wager.getAmount() <= 0) {
                throw new InvalidRequestException("Wager(s) are invalid");
            }
            for (WagerLeg wagerLeg : wager.getLegs()) {
                if (validatedLegs.contains(wagerLeg)) continue;
                ResponseEntity<Integer> response;
                try {
                    response = getOdds(wagerLeg);
                } catch (RestClientException e) {
                    throw new ServiceConnectionException("Failed to connect to Odds server");
                }
                if (response.getBody() == null || !response.getBody().equals(wagerLeg.getOdds())) {
                    throw new InvalidRequestException("Wager odds have changed");
                }
                validatedLegs.add(wagerLeg);
            }
        }
    }

    private Bet createBet(String userId, int odds, double amount) {
        Bet bet = new Bet(userId, amount, odds);
        return betRepository.save(bet);
    }

    private void createLeg(WagerLeg wagerLeg, Bet createdBet) {
        Leg leg = new Leg(createdBet, wagerLeg.getPropId(), wagerLeg.getValue());
        legRepository.save(leg);
    }

    private boolean canParlayLegs(Collection<WagerLeg> legs) {
        // TODO determine if parlay is possible
        return true;
    }

    private int calculateParlayOdds(Collection<WagerLeg> legs) {
        // TODO calculate parlay odds
        return 100;
    }

    private ResponseEntity<Integer> getOdds(WagerLeg wagerLeg) throws RestClientException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        OddsRequestDto body = new OddsRequestDto(wagerLeg.getPropId(), wagerLeg.getValue());
        HttpEntity<OddsRequestDto> requestEntity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(
                "http://" + oddsService + "/props/query",
                HttpMethod.POST,
                requestEntity,
                Integer.class);
    }

    private boolean deductFunds(String userId, Double amount) {
        // TODO
        return true;
    }
}
