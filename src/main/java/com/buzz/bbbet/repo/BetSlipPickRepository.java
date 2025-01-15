package com.buzz.bbbet.repo;

import com.buzz.bbbet.entity.BetSlipPick;
import com.buzz.bbbet.entity.BetSlipPickId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetSlipPickRepository extends JpaRepository<BetSlipPick, BetSlipPickId> {
    List<BetSlipPick> findByIdUserId(String id);
}
