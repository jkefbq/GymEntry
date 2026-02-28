package com.jkefbq.gymentry.kafka.producer;

import com.jkefbq.gymentry.database.dto.GymInfoDto;
import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.database.dto.VisitDto;
import com.jkefbq.gymentry.kafka.VisitEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<@NonNull String, @NonNull Object> kafkaTemplate;
    @Value("${app.kafka.topics.visits}")
    private String VISIT_TOPIC;

    public void sendVisitEvent(SubscriptionDto subscription, GymInfoDto gymInfoDto) {
        kafkaTemplate.send(
                new ProducerRecord<>(
                        VISIT_TOPIC,
                        new VisitEvent(
                                VisitDto.builder()
                                        .gym(gymInfoDto)
                                        .createdAt(LocalDateTime.now())
                                        .subscription(subscription)
                                        .build()
                        )
                )
        );
    }
}
