package com.grydtech.msstack.request.netty.routing;

import com.grydtech.msstack.core.handler.Handler;
import com.grydtech.msstack.request.netty.uri.Endpoint;
import com.grydtech.msstack.request.netty.uri.EndpointMatch;
import com.grydtech.msstack.request.netty.util.EndpointUtils;
import io.netty.handler.codec.http.HttpMethod;

import javax.ws.rs.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class maps each {@link Endpoint} to the respective {@link MethodWrapper}.
 * It enables directing requests with an endpoint, to a method that is designated to handle it.
 * <br/>
 * {@link Router} objects have a {@code route()} method that takes in a URI, and determines
 * the {@link MethodWrapper} that it points to.
 */
public final class Router {

    /**
     * The {@link Set} of {@link Endpoint} objects
     */
    private final Set<Endpoint> endpoints;

    private Router(Set<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

    /**
     * This static method accepts a set of {@link com.grydtech.msstack.core.handler.Handler} classes, traverses through each annotation,
     * and builds a route map for each handler. It generates {@link Endpoint} objects that correspond to each
     * {@link javax.ws.rs.Path} annotation, gets the methods, and wrap them in {@link MethodWrapper} instances.
     *
     * @param handlerClasses The set of handler classes to find endpoints in
     * @return {@link Router} instance with all routes configured
     */
    public static Router build(Set<Class<?>> handlerClasses) {
        Set<Endpoint> endpoints = handlerClasses.stream()
                .filter(Handler.class::isAssignableFrom)
                .map(aClass -> (Class<? extends Handler>) aClass.asSubclass(Handler.class))
                .map(EndpointUtils::extractEndpoint)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        return new Router(endpoints);
    }

    /**
     * Search through registered routes to find the corresponding {@link RoutingResult} for the
     * requested {@link HttpMethod} and {@link Path} as {@link String}
     *
     * @param httpMethod GET, POST, etc.
     * @param path       URI for the Request
     * @return {@link RoutingResult} for the {@code httpMethod} and {@code path}.
     * This object contains the {@link EndpointMatch} and {@link MethodWrapper} for that particular request.
     */
    public final RoutingResult route(HttpMethod httpMethod, String path) throws Exception {
        // TODO use HTTP Method as well for the routes
        httpMethod.name();
        // Find endpoint that matches path
        Endpoint endpoint = endpoints.stream()
                .filter(e -> e.match(path).isPresent())
                .findFirst().orElseThrow(Exception::new);
        // Get the destination EndpointMatch and MethodWrapper
        EndpointMatch endpointMatch = endpoint.match(path).orElseThrow(RuntimeException::new);
        MethodWrapper methodWrapper = endpoint.getMethod();
        // Return RoutingResult
        return new RoutingResult(endpointMatch, methodWrapper);
    }
}
