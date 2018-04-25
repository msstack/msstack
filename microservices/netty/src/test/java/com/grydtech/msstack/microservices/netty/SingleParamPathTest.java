package com.grydtech.msstack.microservices.netty;

import com.grydtech.msstack.microservices.netty.uri.PathMatch;
import com.grydtech.msstack.microservices.netty.uri.PathPattern;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class SingleParamPathTest {

    private final String orderId = "order-id";
    private final String orderIdVal = "453B-289";

    private final PathPattern pathPattern = PathPattern.fromAnnotatedPath(String.format("/orders/{%s}/create", orderId));
    private final PathMatch pathMatch = pathPattern.getPathMatch(String.format("/orders/%s/create", orderIdVal));

    @Test
    public void testPathMatches() {
        Assert.assertNotNull(pathMatch);
    }

    @Test
    public void testPathMatchHasData() {
        Assert.assertNotNull(pathMatch);
    }

    @Test
    public void testPathMatchDataCorrect() {
        Assert.assertNotNull(pathMatch);
        Map<String, List<String>> paramMatches = pathMatch.getParamMatches();
        Assert.assertTrue(paramMatches.size() == 1
                && paramMatches.containsKey(orderId)
                && paramMatches.get(orderId).get(0).equals(orderIdVal)
        );
    }
}
