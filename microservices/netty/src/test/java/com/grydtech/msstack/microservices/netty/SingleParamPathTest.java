package com.grydtech.msstack.microservices.netty;

import com.grydtech.msstack.microservices.netty.uri.PathMatch;
import com.grydtech.msstack.microservices.netty.uri.PathPattern;
import org.junit.Test;

import java.util.Map;

public class SingleParamPathTest {

    private final String orderId = "order-id";
    private final String orderIdVal = "453B-289";

    private final PathPattern pathPattern = PathPattern.fromAnnotatedPath(String.format("/orders/{%s}/create", orderId));
    private final PathMatch pathMatch = pathPattern.getPathMatch(String.format("/orders/%s/create", orderIdVal));

    @Test
    public void testPathMatches() {
        assert pathMatch != null;
    }

    @Test
    public void testPathMatchHasData() {
        assert pathMatch != null;
    }

    @Test
    public void testPathMatchDataCorrect() {
        assert pathMatch != null;
        Map<String, String> paramMatches = pathMatch.getParamMatches();
        assert paramMatches != null
                && paramMatches.size() == 1
                && paramMatches.containsKey(orderId)
                && paramMatches.get(orderId).equals(orderIdVal);
    }
}
