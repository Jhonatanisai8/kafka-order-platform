package com.demo.notification_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.demo.notification_service.dto.OrderEvent;

import org.springframework.messaging.handler.annotation.Header;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationConsumer {
    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void consumeOrderEvent(
            @Payload OrderEvent orderEvent,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {
        log.info("==============================================");
        log.info("Mensaje Recibido desde Kafka");
        log.info("Particion: [{}], Offset: [{}]", partition, offset);
        log.info("ID de orden: {}", orderEvent.getOrderId());
        log.info("Cliente: {}", orderEvent.getCustomerEmail());
        log.info("Producto: {}", orderEvent.getProduct());
        log.info("Monto: S/. {}", orderEvent.getPrice());
        log.info("Estado: {}", orderEvent.getStatus());
        log.info("Simulando envio de correo/comprobante al cliente...");
        log.info("==============================================");
    }
}
