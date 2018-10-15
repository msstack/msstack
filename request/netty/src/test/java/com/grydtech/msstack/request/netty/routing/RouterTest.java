package com.grydtech.msstack.request.netty.routing;

import com.grydtech.msstack.core.types.messaging.Command;
import com.grydtech.msstack.core.types.messaging.Response;
import io.netty.handler.codec.http.HttpMethod;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

public class RouterTest {

    private static Router router;
    private static Command sampleRequest;
    private static Response expectedResponse;

    @BeforeClass
    public static void setUp() {
        UUID uuid = UUID.randomUUID();
        router = Router.build(new HashSet<>(Collections.singletonList(TestHandler.class)));
        sampleRequest = new TestRequest().setId(uuid).setPayload("bar");
        expectedResponse = new TestResponse().setId(uuid).setPayload("foobar");
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
}