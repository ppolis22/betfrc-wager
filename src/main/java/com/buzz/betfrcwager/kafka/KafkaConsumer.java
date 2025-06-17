package com.buzz.betfrcwager.kafka;

import com.buzz.betfrcwager.external.PropResolvedEvent;
import com.buzz.betfrcwager.service.BetResolverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    private final BetResolverService betResolverService;

    public KafkaConsumer(BetResolverService betResolverService) {
        this.betResolverService = betResolverService;
    }

    @KafkaListener(topics = "prop-resolution", groupId = "wager-service")
    public void consumeEvent(@Payload PropResolvedEvent event) {
        logger.info("Consumed prop resolved event. PropId: " + event.getPropId() +
                ", value: " + event.getValue() + ", state: " + event.getState());
        try {
            betResolverService.resolveBets(event.getPropId(), event.getValue(), event.getState());
        } catch (Exception e) {
            logger.error("Error in settling/writing bets:", e);
        }
    }
}
