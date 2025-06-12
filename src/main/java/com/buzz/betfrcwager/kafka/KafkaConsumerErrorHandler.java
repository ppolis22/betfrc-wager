package com.buzz.betfrcwager.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerErrorHandler.class);

    @Bean
    public DefaultErrorHandler errorHandler() {
        // Retry 0 times
        FixedBackOff fixedBackOff = new FixedBackOff(0L, 0);
        ConsumerRecordRecoverer recoverer = (record, ex) -> {
            logger.warn("Deserialization error: " + ex.getMessage());
        };

        return new DefaultErrorHandler(recoverer, fixedBackOff);
    }

}
