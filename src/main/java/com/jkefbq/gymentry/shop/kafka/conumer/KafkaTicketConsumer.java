package com.jkefbq.gymentry.shop.kafka.conumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@EnableKafka
@Service
public class KafkaTicketConsumer {

    @KafkaListener(topics = "tickets")
    public void consumeOrder() {
        log.info("принял в listener");
    }
}
