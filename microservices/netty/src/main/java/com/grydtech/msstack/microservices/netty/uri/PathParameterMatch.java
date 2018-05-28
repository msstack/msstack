package com.grydtech.msstack.microservices.netty.uri;

import java.util.Map;

/**
 * This class wraps the {@link javax.ws.rs.PathParam} matches with their respective {@link String} value
 */
public class PathParameterMatch {

    private final Map<String, String> parameterMatches;

    public PathParameterMatch(Map<String, String> parameterMatches) {
        this.parameterMatches = parameterMatches;
    }

    public Map<String, String> getParameterMatches() {
        return parameterMatches;
    }
}
