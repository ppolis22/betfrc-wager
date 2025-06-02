package com.buzz.bbbet.service;

import com.buzz.bbbet.dto.BetSlipPlaceDto;
import com.buzz.bbbet.dto.Wager;
import com.buzz.bbbet.dto.WagerLeg;
import com.buzz.bbbet.entity.Bet;
import com.buzz.bbbet.entity.Leg;
import com.buzz.bbbet.exception.InvalidRequestException;
import com.buzz.bbbet.external.OddsRequestDto;
import com.buzz.bbbet.repo.BetRepository;
import com.buzz.bbbet.repo.LegRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BetServiceImpl implements BetService {

    private final BetRepository betRepository;
    private final LegRepository legRepository;

    public BetServiceImpl(
            BetRepository betRepository,
            LegRepository legRepository
    ) {
        this.betRepository = betRepository;
        this.legRepository = legRepository;
    }

    @Override
    public List<Bet> handleBetslipPost(BetSlipPlaceDto betslip, String userId) throws InvalidRequestException {
        // verify betslip values match current odds
        Set<WagerLeg> validatedLegs = new HashSet<>();

        // verify all wagers are valid
        for (Wager wager : betslip.getWagers()) {
            if (wager.getLegs().size() == 0 || !canParlayLegs(wager.getLegs()) || wager.getAmount() <= 0) {
                throw new InvalidRequestException("Wager(s) are invalid");
            }
            for (WagerLeg wagerLeg : wager.getLegs()) {
                if (validatedLegs.contains(wagerLeg)) continue;
                ResponseEntity<Integer> response = getOdds(wagerLeg);
                if (response.getBody() == null || !response.getBody().equals(wagerLeg.getOdds())) {
                    throw new InvalidRequestException("Wager odds have changed");
                }
                validatedLegs.add(wagerLeg);
            }
        }

        // try to deduct funds from funds service
        Double betTotal = betslip.getWagers().stream().mapToDouble(Wager::getAmount).sum();
        ResponseEntity<Boolean> deductFundsResponse = deductFunds(userId, betTotal);
        if (!deductFundsResponse.getBody()) {
            throw new InvalidRequestException("Unable to deduct funds");
        }

        // write to bets db (or later, kafka queue), if fails refund funds
        List<Bet> createdBets = new ArrayList<>();
        for (Wager wager : betslip.getWagers()) {
            int odds = wager.getLegs().size() > 1 ? calculateParlayOdds(wager.getLegs()) : wager.getLegs().get(0).getOdds();
            Bet createdBet = createBet(userId, odds, wager);
            wager.getLegs().forEach(leg -> createLeg(leg, createdBet));
            createdBets.add(createdBet);
        }

        return createdBets;
    }

    @Override
    public List<Bet> getBetsForUser(String userId) {
        List<Bet> userBets = betRepository.findByUserId(userId);
        return userBets;
    }

    private Bet createBet(String userId, int odds, Wager wager) {
        Bet bet = new Bet(userId, wager.getAmount(), odds);
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
                "http://localhost:8080/props/query",
                HttpMethod.POST,
                requestEntity,
                Integer.class);
    }

    private ResponseEntity<Boolean> deductFunds(String userId, Double amount) {
        // TODO
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
