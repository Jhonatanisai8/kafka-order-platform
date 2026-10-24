package com.demo.notification_service.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private String orderId;
    private String customerEmail;
    private String product;
    private BigDecimal price;
    private String status;
}
