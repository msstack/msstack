package com.grydtech.msstack.microservices.netty.routing;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * This class is a wrapper for {@link Method} Class, and is used to automatically instantiate objects and inject
 * PathParams and QueryParams before invoking the method.
 */
public final class MethodWrapper {

    /**
     * The {@link Method} that is invoked with route params
     */
    private final Method destinationMethod;

    /**
     * List of Param names in the order of presence in {@code destinationMethod}
     */
    private final List<String> methodSignature;

    /**
     * Mapping from Param ({@link PathParam} or {@link QueryParam}) name to the {@link Field} annotated with it
     */
    private final Map<String, Field> fieldMap;

    /**
     * Mapping from Param ({@link PathParam} or {@link QueryParam}) name to the {@link Type} of Parameter
     */
    private final Map<String, Type> paramMap;

    /**
     * Constructor for {@link MethodWrapper}
     *
     * @param destinationMethod The method to execute when invoking the MethodWrapper
     */
    protected MethodWrapper(Method destinationMethod) {
        this.destinationMethod = destinationMethod;
        // Initialize the Maps and Lists
        methodSignature = new LinkedList<>();
        fieldMap = new HashMap<>();
        paramMap = new HashMap<>();
        // Build the Maps
        this.buildFieldMap();
        this.buildAnnotatedTypeMap();
    }

    /**
     * Add Fields annotated with either {@link PathParam} or {@link QueryParam} to {@code fieldMap}
     */
    private void buildFieldMap() {
        Arrays.stream(destinationMethod.getDeclaringClass().getDeclaredFields())
                .parallel()
                .forEach(field -> {
                    final String param;
                    if (field.isAnnotationPresent(PathParam.class)) {
                        param = field.getDeclaredAnnotation(PathParam.class).value();
                        fieldMap.put(param, field);
                    } else if (field.isAnnotationPresent(QueryParam.class)) {
                        param = field.getDeclaredAnnotation(QueryParam.class).value();
                        fieldMap.put(param, field);
                    }
                });
    }

    /**
     * Add Method Parameters annotated with either {@link PathParam} or {@link QueryParam} to {@code fieldMap}
     */
    private void buildAnnotatedTypeMap() {
        Arrays.stream(destinationMethod.getParameters())
                .sequential()
                .forEach(parameter -> {
                    final String param;
                    if (parameter.isAnnotationPresent(PathParam.class)) {
                        param = parameter.getDeclaredAnnotation(PathParam.class).value();
                    } else if (parameter.isAnnotationPresent(QueryParam.class)) {
                        param = parameter.getDeclaredAnnotation(QueryParam.class).value();
                    } else {
                        return;
                    }
                    methodSignature.add(param);
                    paramMap.put(param, parameter.getType());
                });
    }


    /**
     * Execute the wrapped {@link Method} with the given parameters
     *
     * @param params The parameters extracted from URIs
     */
    public void invoke(Map<String, List<String>> params) {
        // Prepare object
        final Object instance;
        try {
            instance = destinationMethod.getDeclaringClass().newInstance();
            // For each field in object instance, inject either the value from params or null
            fieldMap.forEach((s, field) -> {
                try {
                    field.set(instance, params.getOrDefault(s, null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
            // TODO after instantiation and injecting fields, cache it to increase the performance of subsequent requests

            // Prepare the method arguments
            final List<Object> args = new ArrayList<>();
            // For each argument in method, inject either the value from params or null
            methodSignature.forEach(s ->
                    args.add(params.containsKey(s) ? paramMap.get(s).getClass().cast(params.get(s).get(0)) : null)
            );

            // Invoke the method on instance, with args
            destinationMethod.invoke(instance, args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
