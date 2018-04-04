package com.grydtech.ibuy.orderservice.handlers;

import com.grydtech.ibuy.orderservice.events.OrderItemAddedEvent;
import com.grydtech.ibuy.orderservice.requests.AddOrderItemRequest;
import com.grydtech.ibuy.orderservice.responses.GenericResponse;
import com.grydtech.msstack.core.CommandHandler;
import com.grydtech.msstack.core.annotations.Handler;

import javax.ws.rs.Path;

@Handler
@Path("/add-order-item")
public class AddOrderItemHandler implements CommandHandler<AddOrderItemRequest, GenericResponse> {

    public GenericResponse handle(AddOrderItemRequest addOrderItemRequest) {
        new OrderItemAddedEvent(addOrderItemRequest.getOrderId(), addOrderItemRequest.getItemEntity()).emit();
        return new GenericResponse(200, "success");
    }

}