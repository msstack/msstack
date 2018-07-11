package com.grydtech.msstack.request.netty.uri;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EndpointMatchTest {

    private EndpointMatch endpointMatch;
    private Map<String, String> paramMatches;

    @Before
    public void setUp() {
        System.out.println("Setting Up");
        paramMatches = new HashMap<>();
        paramMatches.put("id", "1");
        endpointMatch = new EndpointMatch(paramMatches);
    }

    @After
    public void tearDown() {
        paramMatches = null;
        endpointMatch = null;
        System.out.println("Done");
    }

    @Test
    public void testParamMatches() {
        Assert.assertEquals(paramMatches, endpointMatch.getParameters());
    }
}