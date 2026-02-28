package com.jkefbq.gymentry.kafka.conumer;

import com.jkefbq.gymentry.kafka.VisitEvent;
import com.jkefbq.gymentry.database.service.VisitService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@EnableKafka
@Service
public class KafkaConsumer {

    private final VisitService visitService;

    public KafkaConsumer(VisitService visitService) {
        this.visitService = visitService;
    }

    @Transactional
    @KafkaListener(topics = "${app.kafka.topics.visits}", containerFactory = "visitEventContainerFactory")
    public void consumeVisitEvent(ConsumerRecord<String, VisitEvent> record) {
        log.info("create visit for user with id {}, subscription id {}", record.value().getVisit().getSubscription().getUserId(),
                record.value().getVisit().getSubscription().getId());
        visitService.create(record.value().getVisit());
    }

}
