package com.grydtech.msstack.microservices.netty.uri;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class PathParameterMatchTest {

    private PathParameterMatch pathParameterMatch;
    private Map<String, String> paramMatches;

    @Before
    public void setUp() {
        System.out.println("Setting Up");
        paramMatches = new HashMap<>();
        paramMatches.put("id", "1");
        pathParameterMatch = new PathParameterMatch(paramMatches);
    }

    @After
    public void tearDown() {
        paramMatches = null;
        pathParameterMatch = null;
        System.out.println("Done");
    }

    @Test
    public void testParamMatches() {
        Assert.assertEquals(paramMatches, pathParameterMatch.getParameterMatches());
    }
}