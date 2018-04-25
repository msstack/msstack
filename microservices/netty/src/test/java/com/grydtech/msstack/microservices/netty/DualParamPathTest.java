package com.grydtech.msstack.microservices.netty;

import com.grydtech.msstack.microservices.netty.uri.PathMatch;
import com.grydtech.msstack.microservices.netty.uri.PathPattern;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class DualParamPathTest {

    private final String orderId = "order-id";
    private final String orderIdVal = "453B-289";


    private final String itemId = "item-id";
    private final String itemIdVal = "PQ-TUV";

    private final PathPattern pathPattern = PathPattern.fromAnnotatedPath(
            String.format("/orders/{%s}/add/{%s}", orderId, itemId)
    );
    private final PathMatch pathMatch = pathPattern.getPathMatch(
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
        Map<String, List<String>> paramMatches = pathMatch.getParamMatches();
        Assert.assertNotNull(paramMatches);
        Assert.assertTrue(paramMatches.size() == 2
                && paramMatches.containsKey(orderId)
                && paramMatches.get(orderId).get(0).equals(orderIdVal)
                && paramMatches.containsKey(itemId)
                && paramMatches.get(itemId).get(0).equals(itemIdVal)
        );
    }
}
