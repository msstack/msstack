package com.grydtech.msstack.request.netty.uri;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class EndpointTest {

    private static final String orderIdKey = "order-id";
    private static final String orderIdVal = "5C";

    private String pathQuery;
    private Pattern pattern;
    private List<String> paramNames;
    private Endpoint endpoint;

    @Before
    public void setUp() {
        System.out.println("Setting Up");
        String annotatedPath = String.format("/orders/{%s}", orderIdKey);
        pathQuery = String.format("/orders/%s", orderIdVal);
        pattern = Pattern.compile("/orders/(?<orderid>[^/]+)");
        paramNames = Collections.singletonList(orderIdKey);
        endpoint = new Endpoint(annotatedPath, null);
    }

    @After
    public void tearDown() {
        endpoint = null;
        System.out.println("Done");
    }

    @Test
    public void fromAnnotatedPath() {
        Assert.assertEquals(paramNames, endpoint.getParamIds());
        Assert.assertEquals(pattern.pattern(), endpoint.getPattern().pattern());
    }

    @Test
    public void getPathMatch() {
        EndpointMatch match = endpoint.match(pathQuery)
                .orElseThrow(RuntimeException::new);
        Assert.assertNotNull(match);
        Map<String, String> paramMatches = match.getParameters();
        Assert.assertEquals(1, paramMatches.size());
        Assert.assertEquals(orderIdVal, paramMatches.get(orderIdKey));
    }
}