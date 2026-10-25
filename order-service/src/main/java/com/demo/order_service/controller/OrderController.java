package com.demo.order_service.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.order_service.dtos.OrderEvent;
import com.demo.order_service.producer.OrderProducer;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderProducer orderProducer;

    @PostMapping
    public ResponseEntity<String> createOrder(
            @RequestBody OrderEvent orderEvent) {
        if (orderEvent.getOrderId() == null ||
                orderEvent.getOrderId().isBlank()) {
            orderEvent.setOrderId(UUID.randomUUID().toString());
        }
        orderEvent.setStatus("PENDING");
        orderProducer.sendOrderEvent(orderEvent);
        return ResponseEntity.ok("Pedido Recibido y enviado a kafka con ID: " + orderEvent.getOrderId());
    }
}
