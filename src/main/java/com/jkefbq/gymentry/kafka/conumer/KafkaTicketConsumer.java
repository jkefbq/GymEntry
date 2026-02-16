package com.jkefbq.gymentry.kafka.conumer;

import com.jkefbq.gymentry.shop.dto.TicketEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@EnableKafka
@Service
public class KafkaTicketConsumer {

    @KafkaListener(topics = "${app.kafka.topics.buy-workout}")
    public void consumeBuyWorkout(ConsumerRecord<String, TicketEvent> record) {


    }
}
