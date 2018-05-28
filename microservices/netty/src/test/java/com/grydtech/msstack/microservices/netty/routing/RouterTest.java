package com.grydtech.msstack.microservices.netty.routing;

import com.grydtech.msstack.core.CommandHandler;
import io.netty.handler.codec.http.HttpMethod;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.Collections;
import java.util.HashSet;

public class RouterTest {
    private static Router router;
    private static TestRequest sampleRequest;
    private static TestResponse expectedResponse;

    @BeforeClass
    public static void setUp() {
        router = Router.build(new HashSet<>(Collections.singletonList(TestHandler.class)));
        expectedResponse = new TestResponse().setResult("foobar");
        sampleRequest = new TestRequest().setId("foo").setData("bar");
    }

    @Test
    public void build() {
        Assert.assertNotNull(router);
    }

    @Test
    public void route() {
        try {
            final String uri = "/test";

            RoutingResult routingResult = router.route(HttpMethod.POST, uri);
            Assert.assertNotNull(routingResult);

            MethodWrapper routedMethod = routingResult.getMethod();
            Assert.assertNotNull(routedMethod);

            Object response = routedMethod.invoke(sampleRequest);
            Assert.assertEquals(expectedResponse, response);

        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Path("/")
    public static class TestHandler implements CommandHandler<TestRequest, TestResponse> {

        @POST
        @Override
        @Path("/test")
        public TestResponse handle(TestRequest request) {
            return new TestResponse().setResult(request.getId() + request.getData());
        }
    }
}