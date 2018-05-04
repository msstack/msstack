package com.grydtech.msstack.microservices.netty.uri;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathMatchTest {

    private PathMatch pathMatch;
    private Map<String, List<String>> paramMatches;

    @Before
    public void setUp() {
        System.out.println("Setting Up");
        paramMatches = new HashMap<>();
        paramMatches.put("id", Collections.singletonList("1"));
        pathMatch = new PathMatch(paramMatches);
    }

    @After
    public void tearDown() {
        paramMatches.clear();
        paramMatches = null;
        pathMatch = null;
        System.out.println("Done");
    }

    @Test
    public void testParamMatches() {
        Assert.assertEquals(paramMatches, pathMatch.getParamMatches());
    }
}