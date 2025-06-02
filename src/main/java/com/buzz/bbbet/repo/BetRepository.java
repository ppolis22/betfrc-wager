package com.buzz.bbbet.repo;

import com.buzz.bbbet.entity.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetRepository extends JpaRepository<Bet, String> {
    List<Bet> findByUserId(String userId);
}
