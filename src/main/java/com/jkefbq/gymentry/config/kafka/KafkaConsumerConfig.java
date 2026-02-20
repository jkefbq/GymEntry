package com.jkefbq.gymentry.config.kafka;

import com.jkefbq.gymentry.props.YamlConfig;
import lombok.NonNull;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<@NonNull String, @NonNull Object> consumerFactory(
            YamlConfig config
    ) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getKafka().getBootstrapServersConfig());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<@NonNull String, @NonNull Object> containerFactory(
            ConsumerFactory<@NonNull String, @NonNull Object> consumerFactory
    ) {
        var containerFactory = new ConcurrentKafkaListenerContainerFactory<@NonNull String, @NonNull Object>();
        containerFactory.setConsumerFactory(consumerFactory);
        return containerFactory;
    }
}
