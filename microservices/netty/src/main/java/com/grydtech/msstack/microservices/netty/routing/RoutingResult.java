package com.grydtech.msstack.microservices.netty.routing;

import com.grydtech.msstack.microservices.netty.uri.PathParameterMatch;

import java.util.Map;

/**
 * This class wraps the {@link PathParameterMatch} and {@link MethodWrapper} that corresponds to a URI
 */
public final class RoutingResult {

    private final PathParameterMatch pathParameterMatch;
    private final MethodWrapper method;

    protected RoutingResult(PathParameterMatch pathParameterMatch, MethodWrapper method) {
        this.pathParameterMatch = pathParameterMatch;
        this.method = method;
    }

    public MethodWrapper getMethod() {
        return method;
    }

    public Class<?> getArgumentClass() {
        return method.getArgumentClass();
    }

    public Map<String, String> getPathParameters() {
        return pathParameterMatch.getParameterMatches();
    }

}
