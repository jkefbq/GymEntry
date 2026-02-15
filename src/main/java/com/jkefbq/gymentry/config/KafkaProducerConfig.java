package com.jkefbq.gymentry.config;

import lombok.NonNull;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<@NonNull String, @NonNull Object> producerFactory() {
        Map<String, Object> configProperties = new HashMap<>();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");//todo
        JacksonJsonSerializer<@NonNull Object> serializer = new JacksonJsonSerializer<>();
        serializer.noTypeInfo();
        return new DefaultKafkaProducerFactory<>(
                configProperties,
                new StringSerializer(),
                serializer
        );
    }

    @Bean
    public KafkaTemplate<@NonNull String, @NonNull Object> kafkaTemplate(
            ProducerFactory<@NonNull String, @NonNull Object> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }
}
