package com.buzz.bbbet.controller;

import com.buzz.bbbet.dto.BetslipDto;
import com.buzz.bbbet.entity.Bet;
import com.buzz.bbbet.external.PropQueryDto;
import com.buzz.bbbet.external.PropResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/bets")
public class BetController {

    private final static ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<>();

    /*
     * TODO convert to submitBetslip() where logged in user's betslip is looked up
     */
    @PostMapping
    public ResponseEntity<Bet> postBet(@RequestBody BetslipDto betslip) {
        String userId = "";

        // get lock on userId
        Lock lock = locks.computeIfAbsent(userId, k -> new ReentrantLock());
        try {
            lock.lock();

            // get current odds from events service (set up cache layer?)
            List<String> propIds = betslip.getBets().stream()
                    .flatMap(bet -> bet.getLegs().stream())
                    .flatMap(leg -> Stream.of(leg.getPropId()))
                    .collect(Collectors.toList());

            PropQueryDto query = new PropQueryDto(propIds);
            RestTemplate restTemplate = new RestTemplate();
            try {
                PropResponseDto response = restTemplate.postForObject(
                        "http://localhost:8080/odds/query",
                        query,
                        PropResponseDto.class);
            } catch (RestClientException e) {
                // TODO
            }

            // confirm odds match
            // try to deduct funds from funds service
            // write to bets db (or kafka queue), if fails refund funds
        } finally {
            // release lock
            lock.unlock();
        }
    }
}
