package com.grydtech.msstack.microservices.netty.routing;

import com.grydtech.msstack.core.GenericHandler;
import io.netty.handler.codec.http.HttpMethod;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.Collections;
import java.util.HashSet;

public class RouterTest {

    private static String paramName;
    private static Object paramValue;
    private static String expectedString;
    private static Router router;

    @BeforeClass
    public static void setUp() {
        router = Router.build(new HashSet<>(Collections.singletonList(TestHandler.class)));
        paramName = "message";
        paramValue = 100;
        expectedString = "Received : %s";
    }

    @Test
    public void build() {
        Assert.assertNotNull(router);
    }

    @Test
    public void route() {
        try {
            RoutingResult routingResult = router.route(
                    HttpMethod.GET,
                    String.format("/test?%s=%s", paramName, paramValue)
            );
            Assert.assertNotNull(routingResult);

            MethodWrapper routedMethod = routingResult.getMethod();
            Assert.assertNotNull(routedMethod);

            Object response = routedMethod.invoke(routingResult.getPathMatch().getParamMatches());
            Assert.assertEquals(String.format(expectedString, paramValue), response);
        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Path("/")
    public static class TestHandler implements GenericHandler<String, String> {
        @GET
        @Path("/test")
        @Override
        public String handle(@QueryParam("message") String arg) {
            return String.format(expectedString, arg);
        }
    }
}