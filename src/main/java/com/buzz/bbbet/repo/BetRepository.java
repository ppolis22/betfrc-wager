package com.buzz.bbbet.repo;

import com.buzz.bbbet.entity.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BetRepository extends JpaRepository<Bet, String> {
}
