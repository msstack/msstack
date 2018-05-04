package com.grydtech.msstack.microservices.netty.uri;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class PathMatchWithDualParamTest {

    private static final String orderId = "order-id", itemId = "item-id";
    private static final String orderIdVal = "453B-289", itemIdVal = "PQ-TUV";

    private static PathPattern pathPattern;
    private static PathMatch pathMatch;

    @Before
    public void setUp() {
        System.out.println("Setting Up");
        pathPattern = PathPattern.fromAnnotatedPath(String.format("/orders/{%s}/add/{%s}", orderId, itemId));
        pathMatch = pathPattern.getPathMatch(String.format("/orders/%s/add/%s", orderIdVal, itemIdVal));
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
            final Map<String, List<String>> paramMatches = pathMatch.getParamMatches();
            Assert.assertNotNull(paramMatches);

            Assert.assertEquals(2, paramMatches.size());

            Assert.assertTrue(paramMatches.containsKey(orderId));
            Assert.assertEquals(orderIdVal, paramMatches.get(orderId).get(0));

            Assert.assertTrue(paramMatches.containsKey(itemId));
            Assert.assertEquals(itemIdVal, paramMatches.get(itemId).get(0));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
