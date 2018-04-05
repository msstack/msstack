package com.grydtech.ibuy.orderservice.events;

import com.grydtech.msstack.core.Event;
import com.grydtech.ibuy.orderservice.entities.ItemEntity;

public class OrderItemAddedEvent extends Event {
    private String orderId;
    private ItemEntity item;

    public OrderItemAddedEvent(String orderId, ItemEntity item) {
        this.orderId = orderId;
        this.item = item;
    }

    public String getOrderId() {
        return orderId;
    }

    public ItemEntity getItem() {
        return item;
    }
}