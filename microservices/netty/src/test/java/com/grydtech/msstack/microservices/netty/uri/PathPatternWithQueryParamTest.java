package com.grydtech.msstack.microservices.netty.uri;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class PathPatternWithQueryParamTest {

    private static final String orderIdKey = "order-id", itemIdKey = "itemId", qtyKey = "qty", unitPriceKey = "unitPrice";
    private static final String orderIdVal = "5C", itemIdVal = "2", qtyVal = "3", unitPriceVal = "500";

    private String annotatedPath;
    private String pathQuery;
    private Pattern pattern;
    private List<String> paramNames;
    private PathPattern pathPattern;

    @Before
    public void setUp() {
        System.out.println("Setting Up");
        annotatedPath = String.format("/orders/{%s}/items", orderIdKey);
        pathQuery = String.format("/orders/%s/items?%s=%s&%s=%s&%s=%s",
                orderIdVal,
                itemIdKey, itemIdVal,
                qtyKey, qtyVal,
                unitPriceKey, unitPriceVal
        );
        pattern = Pattern.compile("/orders/(?<orderid>[^/]+)/items");
        paramNames = Arrays.asList(orderIdKey, itemIdKey, qtyKey, unitPriceKey);
    }

    @After
    public void tearDown() {
        pathPattern = null;
    }

    @Test
    public void fromAnnotatedPath() {
        pathPattern = PathPattern.fromAnnotatedPath(annotatedPath);
        Assert.assertEquals(paramNames.subList(0, 1), pathPattern.getParamNames());
        Assert.assertEquals(pattern.pattern(), pathPattern.getPattern().pattern());
    }

    @Test
    public void getPathMatch() {
        pathPattern = PathPattern.fromAnnotatedPath(annotatedPath);
        PathMatch match = pathPattern.getPathMatch(pathQuery);
        Assert.assertNotNull(match);
        Map<String, List<String>> paramMatches = match.getParamMatches();
        Assert.assertEquals(4, paramMatches.size());
        Assert.assertEquals(orderIdVal, paramMatches.get(orderIdKey).get(0));
        Assert.assertEquals(itemIdVal, paramMatches.get(itemIdKey).get(0));
        Assert.assertEquals(qtyVal, paramMatches.get(qtyKey).get(0));
        Assert.assertEquals(unitPriceVal, paramMatches.get(unitPriceKey).get(0));
    }
}