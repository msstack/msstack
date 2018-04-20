package com.grydtech.msstack.microservices.netty.util;

import java.lang.reflect.Method;
import java.util.Map;

public final class Reflector {

    private Reflector() {
        // private constructor
    }

    public static Map<String, Method> getPathToMethodMap() {
        // Return a map from each Path string to the executable
        // The PathParams and other stuff are found out separately
        return null;
    }
}
