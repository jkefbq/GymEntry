package com.jkefbq.gymentry.shop.kafka.producer;

import com.jkefbq.gymentry.shop.dto.TicketEvent;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaTicketProducer {

    private static final String TOPIC = "tickets";
    private final KafkaTemplate<@NonNull String, @NonNull Object> kafkaTemplate;

    public void sendKafkaEvent(TicketEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}
