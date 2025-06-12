package com.buzz.betfrcwager.service;

import com.buzz.betfrcwager.entity.Bet;
import com.buzz.betfrcwager.entity.BetStatus;
import com.buzz.betfrcwager.entity.Leg;
import com.buzz.betfrcwager.repo.BetRepository;
import com.buzz.betfrcwager.repo.LegRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BetResolverImpl implements BetResolverService {
    private static final Logger logger = LoggerFactory.getLogger(BetResolverImpl.class);

    private final BetRepository betRepository;
    private final LegRepository legRepository;

    public BetResolverImpl(BetRepository betRepository, LegRepository legRepository) {
        this.betRepository = betRepository;
        this.legRepository = legRepository;
    }

    @Override
    public void resolveBets(String propId, String propValue, String state) {
        BetStatus newLegStatus = BetStatus.fromCode(state);
        List<Leg> resolvedLegs = legRepository.findAllByPropIdAndPropValue(propId, propValue);
        for (Leg leg : resolvedLegs) {
            Bet parent = leg.getParent();
            BetStatus oldBetStatus = parent.getStatus();
            List<BetStatus> siblingLegStatuses = parent.getLegs().stream()
                    .filter(l -> !l.getId().equals(leg.getId()))
                    .map(Leg::getStatus)
                    .collect(Collectors.toList());

            if (leg.getStatus().isSettled()) {
                logger.warn("Resolved leg already has settled status - no action.");
            } else {
                leg.setStatus(newLegStatus);
            }

            // If a leg loses, the parent bet loses
            if (newLegStatus == BetStatus.LOST) {
                parent.setStatus(BetStatus.LOST);
            }

            // If a leg wins, the bet wins if all other legs won or voided
            if (newLegStatus == BetStatus.WON &&
                    siblingLegStatuses.stream().allMatch(s -> s == BetStatus.WON || s == BetStatus.VOID)) {
                parent.setStatus(BetStatus.WON);
            }

            // If a leg voids, the bet voids if all other legs are void, and
            // wins if all other legs are win or void with at least one win
            if (newLegStatus == BetStatus.VOID) {
                if (siblingLegStatuses.stream().allMatch(s -> s == BetStatus.VOID)) {
                    parent.setStatus(BetStatus.VOID);
                } else if (siblingLegStatuses.stream().noneMatch(s -> s == BetStatus.OPEN || s == BetStatus.LOST)) {
                    parent.setStatus(BetStatus.WON);
                }
            }

            // TODO update settledDate on Bet
            betRepository.save(parent);

            if (!oldBetStatus.isSettled() && parent.getStatus().isSettled()) {
                logger.info("Leg resolution settled bet: " + parent.getId() +
                        " with status: " + parent.getStatus());
                // TODO write to kafka for payment/notification service
            }
        }
    }
}
