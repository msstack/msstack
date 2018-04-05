package com.grydtech.ibuy.orderservice.events;

import com.grydtech.msstack.core.Event;

public class OrderCreatedEvent extends Event {
    private String orderId;
    private String customerId;

    public OrderCreatedEvent(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public OrderCreatedEvent(String orderId, String customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
    }

    public String getOrderId() {
        return orderId;
    }
}