package com.demo.order_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    public static final String ORDER_TOPIC = "order-events";

    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder.name(ORDER_TOPIC)
                .partitions(3) // 3 particiones para permitir escabilidad horizontal
                .replicas(1) // 1 réplica (suficiente para local)
                .build();
    }
}
