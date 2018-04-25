package com.grydtech.msstack.microservices.netty.uri;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class PathPatternWithoutQueryParamTest {

    private String annotatedPath;
    private String pathQuery;
    private Pattern pattern;
    private List<String> paramNames;
    private String orderId = "5C";

    private PathPattern pathPattern;

    @Before
    public void setUp() {
        System.out.println("Setting Up");
        annotatedPath = "/orders/{order-id}/add";
        pathQuery = String.format("/orders/%s/add", orderId);
        pattern = Pattern.compile("orders/(?<orderid>\\w+)/add");
        paramNames = Collections.singletonList("order-id");
    }

    @After
    public void tearDown() {
        pathPattern = null;
    }

    @Test
    public void fromAnnotatedPath() {
        pathPattern = PathPattern.fromAnnotatedPath(annotatedPath);
        Assert.assertEquals(paramNames, pathPattern.getParamNames());
        Assert.assertEquals(pattern.pattern(), pathPattern.getPattern().pattern());
    }

    @Test
    public void getPathMatch() {
        pathPattern = PathPattern.fromAnnotatedPath(annotatedPath);
        PathMatch match = pathPattern.getPathMatch(pathQuery);
        Assert.assertNotNull(match);
        Map<String, List<String>> paramMatches = match.getParamMatches();
        Assert.assertEquals(1, paramMatches.size());
        Assert.assertEquals(orderId, paramMatches.get("order-id").get(0));
    }
}