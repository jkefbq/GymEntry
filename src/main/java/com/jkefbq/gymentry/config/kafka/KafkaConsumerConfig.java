package com.jkefbq.gymentry.config.kafka;

import com.jkefbq.gymentry.kafka.VisitEvent;
import com.jkefbq.gymentry.props.YamlConfig;
import lombok.NonNull;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("spring.kafka.consumer.group-id")
    private String groupId;

    @Bean
    public ConsumerFactory<@NonNull String, @NonNull VisitEvent> consumerFactory(
            YamlConfig config
    ) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "com.jkefbq.gymentry.*");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getKafka().getBootstrapServersConfig());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<@NonNull String, @NonNull VisitEvent> visitEventContainerFactory(
            ConsumerFactory<@NonNull String, @NonNull VisitEvent> consumerFactory
    ) {
        var containerFactory = new ConcurrentKafkaListenerContainerFactory<@NonNull String, @NonNull VisitEvent>();
        containerFactory.setConsumerFactory(consumerFactory);
        return containerFactory;
    }

}
