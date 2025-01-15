package com.buzz.bbbet.controller;

import com.buzz.bbbet.dto.BetSlipPlaceDto;
import com.buzz.bbbet.dto.BetSlipPostDto;
import com.buzz.bbbet.dto.BetSlipPostResponseDto;
import com.buzz.bbbet.dto.Wager;
import com.buzz.bbbet.entity.Bet;
import com.buzz.bbbet.entity.BetSlipPick;
import com.buzz.bbbet.entity.Leg;
import com.buzz.bbbet.external.Prop;
import com.buzz.bbbet.external.PropQueryDto;
import com.buzz.bbbet.external.PropResponseDto;
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
import java.util.stream.Stream;

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
    public ResponseEntity<List<Bet>> postBet(@RequestBody BetSlipPlaceDto wagers) {
        String userId = "1";

        // get lock on userId
        Lock lock = locks.computeIfAbsent(userId, k -> new ReentrantLock());
        try {
            lock.lock();

            // verify all picks in betslip have a wager (straight or parlay)
            List<BetSlipPick> betSlipPicks = betService.getBetSlipPicks(userId);
            // TODO

            // get current odds from events service (set up cache layer?)
            List<String> propIds = betSlipPicks.stream()
                    .flatMap(pick -> Stream.of(pick.getId().getPropId()))
                    .collect(Collectors.toList());

            Map<String, Prop> currentProps;
            try {
                ResponseEntity<PropResponseDto> response = getCurrentOdds(propIds);
                if (response.getStatusCode().is4xxClientError()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                List<Prop> propsList = response.getBody().getProps();
                currentProps = propsList.stream().collect(Collectors.toMap(
                        Prop::getId,
                        p -> p
                ));
            } catch (RestClientException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // confirm odds match
            boolean upToDate = betSlipPicks.stream().allMatch(p -> {
                Prop match = currentProps.get(p.getId().getPropId());
                if (match == null) return false;
                return match.getOdds().equals(p.getAckOdds()) &&
                        match.getPropValue().equals(p.getAckPropValue());
            });

            if (!upToDate) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            // try to deduct funds from funds service
            Double betTotal = wagers.getWagers().stream().mapToDouble(Wager::getAmount).sum();
            // TODO assume it worked for now

            // write to bets db (or later, kafka queue), if fails refund funds
            List<Bet> createdBets = new ArrayList<>();
            for (Wager wager : wagers.getWagers()) {
                if (wager.getAmount() > 0.0) {
                    Bet createdBet;
                    if (wager.isParlayWager()) {
                        int odds = calculateParlayOdds(currentProps.values());  // TODO handle null
                        createdBet = createBet(userId, odds, wager, "PARLAY");
                        currentProps.values().forEach(prop -> createLeg(prop, createdBet));
                    } else {
                        Prop prop = currentProps.get(wager.getPropId());
                        createdBet = createBet(userId, prop.getOdds(), wager, "STRAIGHT");
                        createLeg(prop, createdBet);
                    }
                    createdBets.add(createdBet);
                }
            }

            // clear betslip
            betService.clearBetSlip(userId);

            return new ResponseEntity<>(createdBets, HttpStatus.CREATED);
        } finally {
            // release lock
            lock.unlock();
        }
    }

    @PostMapping("/betslip")
    public ResponseEntity<BetSlipPostResponseDto> addToBetslip(@RequestBody BetSlipPostDto newPick) {
        String userId = "1";

        // get lock on userId
        Lock lock = locks.computeIfAbsent(userId, k -> new ReentrantLock());
        try {
            lock.lock();

            // add incoming pick to betslip
            betService.savePickToBetSlip(
                    userId, newPick.getPropId(), newPick.getPropValue(), newPick.getPropOdds());

            // if possible to parlay, fetch latest odds and calculate parlay odds
            List<BetSlipPick> picks = betService.getBetSlipPicks(userId);
            List<String> propIds = picks.stream()
                    .flatMap(p -> Stream.of(p.getId().getPropId()))
                    .collect(Collectors.toList());

            PropResponseDto propOdds;
            try {
                ResponseEntity<PropResponseDto> response = getCurrentOdds(propIds);
                if (!response.getStatusCode().is2xxSuccessful()) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                propOdds = response.getBody();
            } catch (RestClientException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Integer parlayOdds = calculateParlayOdds(propOdds.getProps());
            return new ResponseEntity<>(new BetSlipPostResponseDto(parlayOdds), HttpStatus.CREATED);
        } finally {
            // release lock
            lock.unlock();
        }
    }

    private Bet createBet(String userId, int odds, Wager wager, String type) {
        Bet bet = new Bet(null, null, type, userId, wager.getAmount(), odds);
        return betService.saveBet(bet);
    }

    private void createLeg(Prop prop, Bet createdBet) {
        Leg leg = new Leg(null, createdBet, prop.getId(), prop.getPropValue());
        betService.saveLeg(leg);
    }

    private Integer calculateParlayOdds(Collection<Prop> props) {
        // TODO
        // determine if parlay is possible
        // calculate parlay odds if so
        return null;
    }

    private ResponseEntity<PropResponseDto> getCurrentOdds(List<String> propIds) throws RestClientException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        PropQueryDto query = new PropQueryDto(propIds);
        HttpEntity<PropQueryDto> requestEntity = new HttpEntity<>(query, headers);

        return restTemplate.exchange(
                "http://localhost:8080/odds/query",
                HttpMethod.POST,
                requestEntity,
                PropResponseDto.class);
    }
}
