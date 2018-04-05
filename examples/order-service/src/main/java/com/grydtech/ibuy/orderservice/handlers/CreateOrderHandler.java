package com.grydtech.ibuy.orderservice.handlers;

import com.grydtech.ibuy.orderservice.events.OrderCreatedEvent;
import com.grydtech.ibuy.orderservice.requests.OrderCreateRequest;
import com.grydtech.ibuy.orderservice.responses.GenericResponse;
import com.grydtech.msstack.core.CommandHandler;
import com.grydtech.msstack.core.annotations.Handler;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Handler
@Path("/create-order")
public class CreateOrderHandler implements CommandHandler<OrderCreateRequest, GenericResponse> {
    
    public GenericResponse handle(OrderCreateRequest orderCreateRequest) {
        new OrderCreatedEvent(orderCreateRequest.getCustomerId()).emit();
        return new GenericResponse(200, "success");
    }

}