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

    private static final String orderIdKey = "order-id";
    private static final String orderIdVal = "5C";

    private String annotatedPath;
    private String pathQuery;
    private Pattern pattern;
    private List<String> paramNames;
    private PathPattern pathPattern;

    @Before
    public void setUp() {
        System.out.println("Setting Up");
        annotatedPath = String.format("/orders/{%s}", orderIdKey);
        pathQuery = String.format("/orders/%s", orderIdVal);
        pattern = Pattern.compile("/orders/(?<orderid>[^/]+)");
        paramNames = Collections.singletonList(orderIdKey);
    }

    @After
    public void tearDown() {
        pathPattern = null;
        System.out.println("Done");
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
        Assert.assertEquals(orderIdVal, paramMatches.get(orderIdKey).get(0));
    }
}