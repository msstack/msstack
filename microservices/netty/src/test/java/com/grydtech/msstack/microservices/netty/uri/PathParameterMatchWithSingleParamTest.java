package com.grydtech.msstack.microservices.netty.uri;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class PathParameterMatchWithSingleParamTest {

    private static final String orderId = "order-id";
    private static final String orderIdVal = "453B-289";

    private static PathMatcher pathPattern;
    private static PathParameterMatch pathParameterMatch;

    @Before
    public void setUp() {
        System.out.println("Setting Up");
        pathPattern = PathMatcher.fromAnnotatedPath(String.format("/orders/{%s}/create", orderId));
        pathParameterMatch = pathPattern.getPathParameterMatch(String.format("/orders/%s/create", orderIdVal));
    }

    @After
    public void tearDown() {
        pathPattern = null;
        pathParameterMatch = null;
        System.out.println("Done");
    }

    @Test
    public void testPathMatches() {
        Assert.assertNotNull(pathParameterMatch);
    }

    @Test
    public void testParamMatches() {
        try {
            Assert.assertNotNull(pathParameterMatch);
            Map<String, String> paramMatches = pathParameterMatch.getParameterMatches();
            Assert.assertTrue(paramMatches.size() == 1
                    && paramMatches.containsKey(orderId)
                    && paramMatches.get(orderId).contains(orderIdVal)
            );
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
