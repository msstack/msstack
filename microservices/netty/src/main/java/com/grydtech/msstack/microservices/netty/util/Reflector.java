package com.grydtech.msstack.microservices.netty.util;

import com.grydtech.msstack.core.GenericHandler;

import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for obtaining JAX-RS annotated Classes and Methods using Reflection.
 */
public final class Reflector {

    /**
     * Private Constructor
     */
    private Reflector() {
    }

    /**
     * Finds {@link Path} annotations in a given {@link GenericHandler} class and its methods,
     * and create a map from each complete path to the corresponding {@link Method}.
     *
     * @param handlerClass A {@link Class} that extends {@link GenericHandler}
     * @return A mapping from Path String to {@link Method}
     */
    public static Map<String, Method> getRoutes(Class<? extends GenericHandler> handlerClass) {
        // Return a map from each Path string to the executable
        final Map<String, Method> routes = new HashMap<>();
        // The PathParams and other stuff are found out separately
        final Path basePath = handlerClass.getDeclaredAnnotation(Path.class);
        if (basePath != null) {
            Arrays.stream(handlerClass.getDeclaredMethods()).forEach(method -> {
                if (method.isAnnotationPresent(Path.class)) {
                    routes.put(String.format("%s/%s", basePath.value(), method.getAnnotation(Path.class).value()), method);
                }
            });
        }
        return routes;
    }
}
