package com.buzz.betfrcwager.repo;

import com.buzz.betfrcwager.entity.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetRepository extends JpaRepository<Bet, String> {
    List<Bet> findByUserId(String userId);
}
