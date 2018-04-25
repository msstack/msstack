package com.grydtech.msstack.microservices.netty.routing;

import com.grydtech.msstack.microservices.netty.uri.PathMatch;

public class RoutingResult {
    private final PathMatch pathMatch;
    private final MethodWrapper routeDestination;

    RoutingResult(PathMatch pathMatch, MethodWrapper routeDestination) {
        this.pathMatch = pathMatch;
        this.routeDestination = routeDestination;
    }

    public PathMatch getPathMatch() {
        return pathMatch;
    }

    public MethodWrapper getMethod() {
        return routeDestination;
    }
}
