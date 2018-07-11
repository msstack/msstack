package com.grydtech.msstack.request.netty.routing;

import com.grydtech.msstack.request.netty.uri.EndpointMatch;

import java.util.Map;

/**
 * This class wraps the {@link EndpointMatch} and {@link MethodWrapper} that corresponds to a URI
 */
public final class RoutingResult {

    private final EndpointMatch endpointMatch;
    private final MethodWrapper method;

    protected RoutingResult(EndpointMatch endpointMatch, MethodWrapper method) {
        this.endpointMatch = endpointMatch;
        this.method = method;
    }

    public MethodWrapper getMethod() {
        return method;
    }

    public Class<?> getArgClass() {
        return method.getArgClass();
    }

    public Map<String, String> getPathParameters() {
        return endpointMatch.getParameters();
    }

}
