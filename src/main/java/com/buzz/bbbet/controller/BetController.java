package com.buzz.bbbet.controller;

import com.buzz.bbbet.entity.Bet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bets")
public class BetController {

    @PostMapping
    public ResponseEntity<Bet> postBet() {
        // get lock on userId
        //
    }
}
