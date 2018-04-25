package com.grydtech.msstack.microservices.netty.routing;

import com.grydtech.msstack.core.MicroserviceApplication;
import com.grydtech.msstack.microservices.netty.uri.PathPattern;
import com.grydtech.msstack.microservices.netty.util.Reflector;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class Router {

    private final Map<PathPattern, RouteDestination> routeMap;

    public Router() {
        this.routeMap = new HashMap<>();
    }

    public static Router build(MicroserviceApplication application) {
        Router router = new Router();
        application.getHandlers().forEach(handlerClass -> router.registerRoutes(Reflector.getRoutes(handlerClass)));
        return router;
    }

    private void registerRoutes(Map<String, Method> routes) {
        // Register the path patterns in routeMap
        routes.forEach((annotatedPath, method) -> {
            PathPattern pathPattern = PathPattern.fromAnnotatedPath(annotatedPath);
            RouteDestination routeDestination = new RouteDestination(method, pathPattern.getParamNames());
            routeMap.put(pathPattern, routeDestination);
        });
    }

    public RouteDestination findDestinationFor(String path) {
        PathPattern matchedPattern = routeMap.keySet().parallelStream()
                .filter(pathPattern -> pathPattern.getPathMatch(path) != null)
                .findFirst().orElse(null);
        if (matchedPattern == null) {
            return null;
        } else {
            return routeMap.get(matchedPattern);
        }
    }
}
