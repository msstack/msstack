package com.grydtech.ibuy.orderservice.requests;

import com.grydtech.msstack.core.Request;

public class OrderFinalizeRequest extends Request {
    private String orderId;
    private Double payment;

    public String getOrderId() {
        return orderId;
    }

    public Double getPayment() {
        return payment;
    }
}
