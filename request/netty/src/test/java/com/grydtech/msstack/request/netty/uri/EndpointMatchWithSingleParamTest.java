package com.grydtech.msstack.request.netty.uri;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class EndpointMatchWithSingleParamTest {

    private static final String orderId = "order-id";
    private static final String orderIdVal = "453B-289";

    private static Endpoint pathPattern;
    private static EndpointMatch endpointMatch;

    @Before
    public void setUp() {
        System.out.println("Setting Up");
        pathPattern = new Endpoint(String.format("/orders/{%s}/create", orderId), null);
        endpointMatch = pathPattern.match(String.format("/orders/%s/create", orderIdVal))
                .orElseThrow(RuntimeException::new);
    }

    @After
    public void tearDown() {
        pathPattern = null;
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
            Map<String, String> paramMatches = endpointMatch.getParameters();
            Assert.assertTrue(paramMatches.size() == 1
                    && paramMatches.containsKey(orderId)
                    && paramMatches.get(orderId).contains(orderIdVal)
            );
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
