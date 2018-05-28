package com.grydtech.msstack.microservices.netty.uri;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class PathMatcherTest {

    private static final String orderIdKey = "order-id";
    private static final String orderIdVal = "5C";

    private String annotatedPath;
    private String pathQuery;
    private Pattern pattern;
    private List<String> paramNames;
    private PathMatcher pathMatcher;

    @Before
    public void setUp() {
        System.out.println("Setting Up");
        annotatedPath = String.format("/orders/{%s}", orderIdKey);
        pathQuery = String.format("/orders/%s", orderIdVal);
        pattern = Pattern.compile("/orders/(?<orderid>[^/]+)");
        paramNames = Collections.singletonList(orderIdKey);
        pathMatcher = PathMatcher.fromAnnotatedPath(annotatedPath);
    }

    @After
    public void tearDown() {
        pathMatcher = null;
        System.out.println("Done");
    }

    @Test
    public void fromAnnotatedPath() {
        Assert.assertEquals(paramNames, pathMatcher.getPathParamNames());
        Assert.assertEquals(pattern.pattern(), pathMatcher.getPathPattern().pattern());
    }

    @Test
    public void getPathMatch() {
        PathParameterMatch match = pathMatcher.getPathParameterMatch(pathQuery);
        Assert.assertNotNull(match);
        Map<String, String> paramMatches = match.getParameterMatches();
        Assert.assertEquals(1, paramMatches.size());
        Assert.assertEquals(orderIdVal, paramMatches.get(orderIdKey));
    }
}