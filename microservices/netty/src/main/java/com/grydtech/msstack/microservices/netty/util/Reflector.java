package com.grydtech.msstack.microservices.netty.util;

import com.grydtech.msstack.core.GenericHandler;

import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class Reflector {

    private Reflector() {
        // private constructor
    }

    public static Map<String, Method> getRoutes(Class<? extends GenericHandler> handlerClass) {
        // Return a map from each Path string to the executable
        // The PathParams and other stuff are found out separately
        final Path basePath = handlerClass.getAnnotation(Path.class);
        if (basePath != null) {
        }
        return new HashMap<>();
    }
}
