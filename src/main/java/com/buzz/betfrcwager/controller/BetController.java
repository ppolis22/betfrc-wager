package com.buzz.betfrcwager.controller;

import com.buzz.betfrcwager.dto.*;
import com.buzz.betfrcwager.entity.Bet;
import com.buzz.betfrcwager.exception.InvalidRequestException;
import com.buzz.betfrcwager.security.jwt.JwtUserDetails;
import com.buzz.betfrcwager.service.BetService;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
