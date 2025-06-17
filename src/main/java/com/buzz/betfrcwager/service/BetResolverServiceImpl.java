package com.buzz.betfrcwager.service;

import com.buzz.betfrcwager.entity.Bet;
import com.buzz.betfrcwager.entity.BetStatus;
import com.buzz.betfrcwager.entity.Leg;
import com.buzz.betfrcwager.repo.BetRepository;
import com.buzz.betfrcwager.repo.LegRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BetResolverServiceImpl implements BetResolverService {
    private static final Logger logger = LoggerFactory.getLogger(BetResolverServiceImpl.class);

    private final BetRepository betRepository;
    private final LegRepository legRepository;

    public BetResolverServiceImpl(BetRepository betRepository, LegRepository legRepository) {
        this.betRepository = betRepository;
        this.legRepository = legRepository;
    }

    @Override
    @Transactional
    public void resolveBets(String propId, String propValue, String state) {
        BetStatus newLegStatus = BetStatus.fromCode(state);
        List<Leg> resolvedLegs = legRepository.findAllByPropIdAndPropValue(propId, propValue);
        logger.info(resolvedLegs.size() + " legs found for prop: " + propId);

        for (Leg resolvedLeg : resolvedLegs) {
            Bet parent = resolvedLeg.getParent();
            BetStatus oldBetStatus = parent.getStatus();
            List<BetStatus> siblingLegStatuses = parent.getLegs().stream()
                    .filter(leg -> !leg.getId().equals(resolvedLeg.getId()))
                    .map(Leg::getStatus)
                    .collect(Collectors.toList());

            if (resolvedLeg.getStatus().isSettled()) {
                logger.warn("Resolved leg already has settled status - no action.");
            } else {
                resolvedLeg.setStatus(newLegStatus);
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
            logger.info("Writing leg status: " + resolvedLeg.getStatus() +
                    ", and parent status: " + parent.getStatus());
            betRepository.save(parent);
            legRepository.save(resolvedLeg);

            if (!oldBetStatus.isSettled() && parent.getStatus().isSettled()) {
                logger.info("Leg resolution settled bet: " + parent.getId() +
                        " with status: " + parent.getStatus());
                // TODO write to kafka for payment/notification service
            }
        }
    }
}
