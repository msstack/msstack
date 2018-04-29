package com.grydtech.msstack.microservices.netty.routing;

import com.grydtech.msstack.core.GenericHandler;
import com.grydtech.msstack.microservices.netty.uri.PathMatch;
import com.grydtech.msstack.microservices.netty.uri.PathPattern;
import com.grydtech.msstack.microservices.netty.util.Reflector;
import io.netty.handler.codec.http.HttpMethod;

import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class maps each {@link PathPattern} to the respective {@link MethodWrapper}.
 * It enables directing requests with an endpoint, to a method that is designated to handle it.
 * <br/>
 * {@link Router} objects have a {@code route()} method that takes in a URI, and determines
 * the {@link MethodWrapper} that it points to.
 */
public final class Router {

    /**
     * The {@link Map} that links each {@link PathPattern} to a {@link MethodWrapper}
     */
    private final Map<PathPattern, MethodWrapper> routeMap;

    private Router() {
        this.routeMap = new HashMap<>();
    }

    /**
     * This static method accepts a set of {@link GenericHandler} classes, traverses through each annotation,
     * and builds a route map for each handler. It generates {@link PathPattern} objects that correspond to each
     * {@link javax.ws.rs.Path} annotation, gets the methods, and wrap them in {@link MethodWrapper} instances.
     *
     * @param handlerClasses The set of handler classes which should be traversed
     * @return {@link Router} instance with all routes configured
     */
    public static Router build(Set<Class<? extends GenericHandler>> handlerClasses) {
        Router router = new Router();
        handlerClasses.forEach(handlerClass ->
                Reflector.getRoutes(handlerClass).forEach((s, method) ->
                        router.routeMap.put(PathPattern.fromAnnotatedPath(s), new MethodWrapper(method))
                )
        );
        return router;
    }

    /**
     * Search through registered routes to find the corresponding {@link RoutingResult} for the
     * requested {@link HttpMethod} and {@link Path} as {@link String}
     *
     * @param httpMethod GET, POST, etc.
     * @param path       URI for the Request
     * @return {@link RoutingResult} for the {@code httpMethod} and {@code path}.
     * This object contains the {@link PathMatch} and {@link MethodWrapper} for that particular request.
     */
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
