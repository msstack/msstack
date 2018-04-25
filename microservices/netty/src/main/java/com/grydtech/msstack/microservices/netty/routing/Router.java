package com.grydtech.msstack.microservices.netty.routing;

import com.grydtech.msstack.core.GenericHandler;
import com.grydtech.msstack.microservices.netty.uri.PathPattern;
import com.grydtech.msstack.microservices.netty.util.Reflector;
import io.netty.handler.codec.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class Router {

    private final Map<PathPattern, MethodWrapper> routeMap;

    private Router() {
        this.routeMap = new HashMap<>();
    }

    public static Router build(Set<Class<? extends GenericHandler>> handlerClasses) {
        Router router = new Router();
        handlerClasses.forEach(handlerClass ->
                Reflector.getRoutes(handlerClass).forEach((s, method) ->
                        router.routeMap.put(PathPattern.fromAnnotatedPath(s), new MethodWrapper(method))
                )
        );
        return router;
    }

    public RoutingResult route(HttpMethod httpMethod, String path) {
        httpMethod.name();
        // This code is written under the assumption that there is only one pathPattern that matches the path
        PathPattern match = routeMap.keySet().parallelStream()
                .filter(pathPattern -> pathPattern.getPathMatch(path) != null)
                .findFirst().orElse(null);
        if (match == null) {
            return null;
        } else {
            return new RoutingResult(match.getPathMatch(path), routeMap.get(match));
        }
    }
}
