package com.grydtech.msstack.microservices.netty.uri;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class PathMatchWithSingleParamTest {

    private static final String orderId = "order-id";
    private static final String orderIdVal = "453B-289";

    private static PathPattern pathPattern;
    private static PathMatch pathMatch;

    @Before
    public void setUp() {
        System.out.println("Setting Up");
        pathPattern = PathPattern.fromAnnotatedPath(String.format("/orders/{%s}/create", orderId));
        pathMatch = pathPattern.getPathMatch(String.format("/orders/%s/create", orderIdVal));
    }

    @After
    public void tearDown() {
        pathPattern = null;
        pathMatch = null;
        System.out.println("Done");
    }

    @Test
    public void testPathMatches() {
        Assert.assertNotNull(pathMatch);
    }

    @Test
    public void testParamMatches() {
        try {
            Assert.assertNotNull(pathMatch);
            Map<String, List<String>> paramMatches = pathMatch.getParamMatches();
            Assert.assertTrue(paramMatches.size() == 1
                    && paramMatches.containsKey(orderId)
                    && paramMatches.get(orderId).contains(orderIdVal)
            );
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
