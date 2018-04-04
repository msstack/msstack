package com.grydtech.ibuy.orderservice.entities;

import com.grydtech.ibuy.orderservice.events.OrderItemAddedEvent;
import com.grydtech.ibuy.orderservice.events.OrderCreatedEvent;
import com.grydtech.ibuy.orderservice.events.OrderFinalizedEvent;

import java.util.List;

public class OrderEntity {
    private String orderId;
    private String customerId;
    private Double payment;
    private List<ItemEntity> items;

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Double getPayment() {
        return payment;
    }

    public List<ItemEntity> getItems() {
        return items;
    }

    public void apply(OrderCreatedEvent orderCreatedEvent) {
        // add OrderCreatedEvent based logic here
    }

    public void apply(OrderItemAddedEvent orderItemAddedEvent) {
        // add ItemAddedEvent based logic here
    }

    public void apply(OrderFinalizedEvent orderFinalizedEvent) {
        // add OrderFinalizedEvent based logic here
    }
}
