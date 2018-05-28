package com.grydtech.msstack.microservices.netty.uri;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class PathParameterMatchWithDualParamTest {

    private static final String orderId = "order-id", itemId = "item-id";
    private static final String orderIdVal = "453B-289", itemIdVal = "PQ-TUV";

    private static PathMatcher pathPattern;
    private static PathParameterMatch pathParameterMatch;

    @Before
    public void setUp() {
        System.out.println("Setting Up");
        pathPattern = PathMatcher.fromAnnotatedPath(String.format("/orders/{%s}/add/{%s}", orderId, itemId));
        pathParameterMatch = pathPattern.getPathParameterMatch(String.format("/orders/%s/add/%s", orderIdVal, itemIdVal));
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
            final Map<String, String> paramMatches = pathParameterMatch.getParameterMatches();
            Assert.assertNotNull(paramMatches);

            Assert.assertEquals(2, paramMatches.size());

            Assert.assertTrue(paramMatches.containsKey(orderId));
            Assert.assertEquals(orderIdVal, paramMatches.get(orderId));

            Assert.assertTrue(paramMatches.containsKey(itemId));
            Assert.assertEquals(itemIdVal, paramMatches.get(itemId));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
