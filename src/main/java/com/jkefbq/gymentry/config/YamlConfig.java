package com.jkefbq.gymentry.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app")
@NoArgsConstructor
public class YamlConfig {

    private Kafka kafka;
    private Tariff tariff;

    @Getter
    @Setter
    @NoArgsConstructor
    static class Tariff {
        private Basic basic;

        @Getter
        @Setter
        @NoArgsConstructor
        static class Basic {
            private BigDecimal pricePerWorkout;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    static class Kafka {
        private String bootstrapServersConfig;
    }
}
