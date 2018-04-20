package com.grydtech.msstack.microservices.netty.routing;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.grydtech.msstack.core.MicroserviceApplication;
import com.grydtech.msstack.microservices.netty.uri.PathPattern;
import com.grydtech.msstack.microservices.netty.util.Reflector;

import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.util.Map;

public final class Router {

    private final ImmutableListMultimap.Builder<PathPattern, RouteDestination> routeMapBuilder;

    private ImmutableListMultimap<PathPattern, RouteDestination> routeMap;

    public Router() {
        this.routeMapBuilder = ImmutableListMultimap.builder();
    }

    public static Router build(MicroserviceApplication application) {
        application.getHandlers().forEach(handlerClass -> {
            final Path path = handlerClass.getAnnotation(Path.class);
            if (path != null) {
                final String pathValue = path.value();
                try {
//					mapPathToClass(pathValue, handlerClass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Router router = new Router();
        Map<String, Method> pathToMethodMap = Reflector.getPathToMethodMap();
        router.routeMap = router.routeMapBuilder.build();
        return router;
    }

    public Router addRoute(PathPattern pathPattern, RouteDestination routeDestination) {
        this.routeMapBuilder.put(pathPattern, routeDestination);
        return this;
    }

    /**
     * Return an Immutable List of RouteDestinations that getPathMatch the given PathPattern
     *
     * @param pathPattern PathPattern to find the PathPattern Destinations
     * @return PathPattern destinations that getPathMatch the pathPattern
     */
    public ImmutableList<RouteDestination> getMatchingDestinations(PathPattern pathPattern) {
        ImmutableList.Builder<RouteDestination> destinationBuilder = new ImmutableList.Builder<>();
        // Find amd return matching RouteDestinations and add them to immutable list
        return routeMap.get(pathPattern).asList();
    }
}
