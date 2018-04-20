package com.grydtech.msstack.microservices.netty.routing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class RouteDestination {

    private final Class destinationClass;
    private final Method destinationMethod;
    private final List<String> paramNames;

    private Map<String, String> paramNameToFieldNameMap;
    private Map<String, String> paramNameToMethodArgNameMap;

    public RouteDestination(Class destinationClass, Method destinationMethod, List<String> paramNames) {
        this.destinationClass = destinationClass;
        this.destinationMethod = destinationMethod;
        this.paramNames = paramNames;
        this.mapParams();
    }

    private void mapParams() {
        // Use reflection to figure out the field names and method argument names
    }

    public void execute() throws InvocationTargetException, IllegalAccessException {
        // Instantiate classes, do all stuff, and execute method
        destinationMethod.invoke(null, null);
    }
}
