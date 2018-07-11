package com.grydtech.msstack.request.netty.routing;

import io.netty.handler.codec.http.HttpMethod;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class RouterTest {

    private static Router router;
    private static TestRequest sampleRequest;
    private static TestResponse expectedResponse;

    @BeforeClass
    public static void setUp() {
        router = Router.build(new HashSet<>(Collections.singletonList(TestHandler.class)));
        sampleRequest = new TestRequest().setId("foo").setData("bar");
        expectedResponse = new TestResponse().setResult("foobar");
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

    @Test
    public void testGenericParameter() {
        Method method = TestHandler.class.getDeclaredMethods()[0];
        System.out.println(Arrays.toString(method.getParameterTypes()));

    }
}