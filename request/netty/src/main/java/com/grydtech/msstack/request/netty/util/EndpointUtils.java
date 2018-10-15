package com.grydtech.msstack.request.netty.util;

import com.grydtech.msstack.core.handler.Handler;
import com.grydtech.msstack.request.netty.routing.MethodWrapper;
import com.grydtech.msstack.request.netty.uri.Endpoint;

import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * Utility class for obtaining JAX-RS annotated Classes and Methods using Reflection.
 */
public final class EndpointUtils {

    private EndpointUtils() {

    }

    /**
     * Finds {@link Path} policy in a given {@link com.grydtech.msstack.core.handler.Handler} class and its methods,
     * and create a map from each complete path to the corresponding {@link Method}.
     *
     * @param handlerClass A {@link Class} that extends {@link com.grydtech.msstack.core.handler.Handler}
     * @return A mapping from Path String to {@link Method}
     */
    public static Optional<Endpoint> extractEndpoint(Class<? extends Handler> handlerClass) {
        // Base URI
        final Path baseURI = handlerClass.getDeclaredAnnotation(Path.class);
        // If no Path annotation found, return empty
        if (baseURI == null) return Optional.empty();
        // Else wrap the method and path into an endpoint and return it
        return Arrays.stream(handlerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Path.class) && !method.isBridge())
                .map(method -> {
                    Path methodURI = method.getAnnotation(Path.class);
                    String fullPath = UriUtils.canonicalize(baseURI.value() + '/' + methodURI.value());
                    return new Endpoint(fullPath, new MethodWrapper(method));
                }).findFirst();
    }
}
