package com.buzz.bbbet.controller;

import com.buzz.bbbet.dto.*;
import com.buzz.bbbet.entity.Bet;
import com.buzz.bbbet.entity.Leg;
import com.buzz.bbbet.external.OddsRequestDto;
import com.buzz.bbbet.service.BetService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bets")
public class BetController {

    private final static ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<>();

    private final BetService betService;

    // TODO move method logic into BetService

    public BetController(BetService betService) {
        this.betService = betService;
    }

    @PostMapping("/submit")
    public ResponseEntity<List<Bet>> postBet(@RequestBody BetSlipPlaceDto betslip) {
        // TODO get userId
        String userId = "1";

        Lock lock = locks.computeIfAbsent(userId, k -> new ReentrantLock());
        try {
            lock.lock();

            // verify betslip values match current odds
            Set<WagerLeg> wagerLegs = betslip.getWagers().stream()
                    .flatMap(w -> w.getLegs().stream())
                    .collect(Collectors.toSet());

            for (WagerLeg wagerLeg : wagerLegs) {
                ResponseEntity<Integer> response = getOdds(wagerLeg);
                if (response.getBody() == null || !response.getBody().equals(wagerLeg.getOdds())) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }

            // verify all wagers are valid
            for (Wager wager : betslip.getWagers()) {
                if (wager.getLegs().size() == 0 || !canParlayLegs(wager.getLegs()) || wager.getAmount() <= 0) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }

            // try to deduct funds from funds service
            Double betTotal = betslip.getWagers().stream().mapToDouble(Wager::getAmount).sum();
            ResponseEntity<Boolean> deductFundsResponse = deductFunds(userId, betTotal);
            if (!deductFundsResponse.getBody()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // write to bets db (or later, kafka queue), if fails refund funds
            List<Bet> createdBets = new ArrayList<>();
            for (Wager wager : betslip.getWagers()) {
                int odds = wager.getLegs().size() > 1 ? calculateParlayOdds(wager.getLegs()) : wager.getLegs().get(0).getOdds();
                Bet createdBet = createBet(userId, odds, wager);
                wager.getLegs().forEach(leg -> createLeg(leg, createdBet));
                createdBets.add(createdBet);
            }

            return new ResponseEntity<>(createdBets, HttpStatus.CREATED);
        } finally {
            // release lock
            lock.unlock();
        }
    }

    private Bet createBet(String userId, int odds, Wager wager) {
        Bet bet = new Bet(userId, wager.getAmount(), odds);
        return betService.saveBet(bet);
    }

    private void createLeg(WagerLeg wagerLeg, Bet createdBet) {
        Leg leg = new Leg(createdBet, wagerLeg.getPropId(), wagerLeg.getValue());
        betService.saveLeg(leg);
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
