package com.grydtech.ibuy.orderservice.requests;

import com.grydtech.ibuy.orderservice.entities.ItemEntity;
import com.grydtech.msstack.core.Request;

public class AddOrderItemRequest extends Request {
    private String orderId;
    private ItemEntity itemEntity;

    public String getOrderId() {
        return orderId;
    }

    public ItemEntity getItemEntity() {
        return itemEntity;
    }
}