package com.jkefbq.gymentry.kafka.conumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.stereotype.Service;

@Slf4j
@EnableKafka
@Service
public class KafkaTicketConsumer {

//    @KafkaListener(topics = "${app.kafka.topics.buy-workout}")
//    public void consumeBuyWorkout(ConsumerRecord<String, TicketEvent> record) {
//
//    }
}
