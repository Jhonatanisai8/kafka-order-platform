package com.demo.order_service.producer;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.demo.order_service.config.KafkaTopicConfig;
import com.demo.order_service.dtos.OrderEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void sendOrderEvent(OrderEvent event) {
        log.info("Publicando evento de orden: {}", event);

        CompletableFuture<SendResult<String, OrderEvent>> future = kafkaTemplate.send(KafkaTopicConfig.ORDER_TOPIC,
                event.getOrderId(), event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Evento enviado con éxito. Offset: [{}], Partición: [{}]",
                        result.getRecordMetadata().offset(),
                        result.getRecordMetadata().partition());
            } else {
                log.error("Fallo al enviar el evento: {}", ex.getMessage());
            }
        });
    }

}
