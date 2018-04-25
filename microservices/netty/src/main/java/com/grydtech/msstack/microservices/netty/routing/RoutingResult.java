package com.grydtech.msstack.microservices.netty.routing;

import com.grydtech.msstack.microservices.netty.uri.PathMatch;

/**
 * This class wraps the {@link PathMatch} and {@link MethodWrapper} that corresponds to a URI
 */
public final class RoutingResult {

    private final PathMatch pathMatch;
    private final MethodWrapper methodWrapper;

    protected RoutingResult(PathMatch pathMatch, MethodWrapper methodWrapper) {
        this.pathMatch = pathMatch;
        this.methodWrapper = methodWrapper;
    }

    public PathMatch getPathMatch() {
        return pathMatch;
    }

    public MethodWrapper getMethod() {
        return methodWrapper;
    }
}
