package com.grydtech.ibuy.orderservice.events;

import com.grydtech.msstack.core.Event;

public class OrderFinalizedEvent extends Event {
    private String orderId;
    private Double payment;

    public OrderFinalizedEvent(String orderId, Double payment) {
        this.orderId = orderId;
        this.payment = payment;
    }

    public String getOrderId() {
        return orderId;
    }

    public Double getPayment() {
        return payment;
    }
}