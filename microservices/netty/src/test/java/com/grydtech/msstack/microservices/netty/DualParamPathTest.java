package com.grydtech.msstack.microservices.netty;

import com.grydtech.msstack.microservices.netty.uri.PathMatch;
import com.grydtech.msstack.microservices.netty.uri.PathPattern;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class DualParamPathTest {

    private static final String orderId = "order-id", itemId = "item-id";
    private static final String orderIdVal = "453B-289", itemIdVal = "PQ-TUV";

    private static final PathPattern pathPattern = PathPattern.fromAnnotatedPath(
            String.format("/orders/{%s}/add/{%s}", orderId, itemId)
    );
    private static final PathMatch pathMatch = pathPattern.getPathMatch(
            String.format("/orders/%s/add/%s", orderIdVal, itemIdVal)
    );

    @Test
    public void testPathMatches() {
        Assert.assertNotNull(pathMatch);
    }

    @Test
    public void testPathMatchIsNotNull() {
        Assert.assertNotNull(pathMatch);
        Assert.assertNotNull(pathMatch.getParamMatches());
    }

    @Test
    public void testPathMatchContainsValue() {
        Assert.assertNotNull(pathMatch);
        final Map<String, List<String>> paramMatches = pathMatch.getParamMatches();

        Assert.assertNotNull(paramMatches);
        Assert.assertEquals(2, paramMatches.size());

        Assert.assertTrue(paramMatches.containsKey(orderId));
        Assert.assertEquals(orderIdVal, paramMatches.get(orderId).get(0));

        Assert.assertTrue(paramMatches.containsKey(itemId));
        Assert.assertEquals(itemIdVal, paramMatches.get(itemId).get(0));
    }
}
