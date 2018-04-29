package com.grydtech.msstack.microservices.netty.uri;

import java.util.List;
import java.util.Map;

/**
 * This class wraps the {@link javax.ws.rs.PathParam} and {@link javax.ws.rs.QueryParam} matches
 * with their respective {@link List} of value(s)
 */
public class PathMatch {

    private final Map<String, List<String>> paramMatches;

    public PathMatch(Map<String, List<String>> paramMatches) {
        this.paramMatches = paramMatches;
    }

    public Map<String, List<String>> getParamMatches() {
        return paramMatches;
    }
}
