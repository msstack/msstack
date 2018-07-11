package com.grydtech.msstack.request.netty.routing;

import com.grydtech.msstack.core.handler.CommandHandler;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/")
public class TestHandler implements CommandHandler<TestRequest, TestResponse> {

    @POST
    @Override
    @Path("/test")
    public TestResponse handle(TestRequest request) {
        return new TestResponse().setResult(request.getId() + request.getData());
    }
}