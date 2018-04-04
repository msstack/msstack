package com.grydtech.ibuy.orderservice;

import com.grydtech.msstack.core.MicroserviceRunner;
import com.grydtech.msstack.core.annotations.Microservice;
import com.grydtech.msstack.microservices.jersey.JerseyApplication;

import javax.ws.rs.ApplicationPath;

@Microservice(port = "4000")
@ApplicationPath("/")
public class OrderServiceApplication extends JerseyApplication {

    public static void main(String[] args) throws Exception {
        MicroserviceRunner.run(OrderServiceApplication.class);
    }
}
