package com.buzz.bbbet.controller;

import com.buzz.bbbet.dto.*;
import com.buzz.bbbet.entity.Bet;
import com.buzz.bbbet.entity.Leg;
import com.buzz.bbbet.exception.InvalidRequestException;
import com.buzz.bbbet.external.OddsRequestDto;
import com.buzz.bbbet.security.jwt.JwtUserDetails;
import com.buzz.bbbet.service.BetService;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public BetController(BetService betService) {
        this.betService = betService;
    }

    @PostMapping("/submit")
    @Secured({ "ROLE_USER" })
    public ResponseEntity<List<Bet>> postBet(@RequestBody BetSlipPlaceDto betslip) {
        String userId = getUserId();
        Lock lock = locks.computeIfAbsent(userId, k -> new ReentrantLock());

        try {
            lock.lock();

            List<Bet> savedBets;
            try {
                savedBets = betService.handleBetslipPost(betslip, userId);
            } catch (InvalidRequestException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(savedBets, HttpStatus.CREATED);
        } finally {
            // release lock
            lock.unlock();
        }
    }

    private String getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();
        return userDetails.getId();
    }
}
