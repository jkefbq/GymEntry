package com.jkefbq.gymentry.shop.service;

import com.jkefbq.gymentry.shop.dto.TicketEvent;
import com.jkefbq.gymentry.kafka.producer.KafkaTicketProducer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubscriptionService {

    private final KafkaTicketProducer kafkaTicketProducer;

    public void buyTicket(TicketEvent ticketEvent) {

    }
}
