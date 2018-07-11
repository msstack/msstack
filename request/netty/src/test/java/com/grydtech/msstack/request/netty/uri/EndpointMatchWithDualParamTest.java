package com.grydtech.msstack.request.netty.uri;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class EndpointMatchWithDualParamTest {

    private static final String orderId = "order-id", itemId = "item-id";
    private static final String orderIdVal = "453B-289", itemIdVal = "PQ-TUV";

    private static Endpoint endpoint;
    private static EndpointMatch endpointMatch;

    @Before
    public void setUp() {
        System.out.println("Setting Up");
        endpoint = new Endpoint(String.format("/orders/{%s}/add/{%s}", orderId, itemId), null);
        endpointMatch = endpoint.match(String.format("/orders/%s/add/%s", orderIdVal, itemIdVal))
                .orElseThrow(RuntimeException::new);
    }

    @After
    public void tearDown() {
        endpoint = null;
        endpointMatch = null;
        System.out.println("Done");
    }

    @Test
    public void testPathMatches() {
        Assert.assertNotNull(endpointMatch);
    }

    @Test
    public void testParamMatches() {
        try {
            Assert.assertNotNull(endpointMatch);
            final Map<String, String> paramMatches = endpointMatch.getParameters();
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
