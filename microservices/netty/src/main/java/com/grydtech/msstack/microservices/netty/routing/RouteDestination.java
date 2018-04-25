package com.grydtech.msstack.microservices.netty.routing;

import javax.ws.rs.PathParam;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Stream;

public class RouteDestination {

    private final Method destinationMethod;
    private final List<String> paramNames;

    private final Class declaringClass;
    private final AnnotatedType[] annotatedParameterTypes;

    private final Map<String, Field> paramNameToFieldMap;
    private final Map<String, AnnotatedType> paramNameToAnnotatedTypeMap;

    public RouteDestination(Method destinationMethod, List<String> paramNames) {
        this.destinationMethod = destinationMethod;
        this.declaringClass = destinationMethod.getDeclaringClass();
        this.paramNames = paramNames;
        this.annotatedParameterTypes = destinationMethod.getAnnotatedParameterTypes();
        // Initialize hash maps
        paramNameToFieldMap = new HashMap<>();
        paramNameToAnnotatedTypeMap = new HashMap<>();
        // Create mappping
        this.mapParams();
    }

    private void mapParams() {
        // Find the Fields and AnnotatedTypes
        final Stream<Field> classFields = Arrays.stream(declaringClass.getDeclaredFields())
                .parallel()
                .filter(field -> field.getAnnotation(PathParam.class) != null);
        final Stream<AnnotatedType> methodAnnotatedTypes = Arrays.stream(annotatedParameterTypes)
                .parallel()
                .filter(annotatedType -> annotatedType.getAnnotation(PathParam.class) != null);
        paramNames.forEach(paramName -> {
            // Update paramNameToFieldMap
            classFields.filter(f -> f.getAnnotation(PathParam.class).value().equals(paramName))
                    .findFirst()
                    .ifPresent(field -> paramNameToFieldMap.put(paramName, field));
            // Update paramNameToAnnotatedTypeMap
            methodAnnotatedTypes.filter(m -> m.getAnnotation(PathParam.class).value().equals(paramName))
                    .findFirst()
                    .ifPresent(annotatedType -> paramNameToAnnotatedTypeMap.put(paramName, annotatedType));
        });
    }

    public void invoke(Map<String, String> paramValues)
            throws InvocationTargetException, IllegalAccessException, InstantiationException {
        // TODO For now, always instantiate class before invoked. Should cache the instances for optimization.
        final Object instance = declaringClass.newInstance();
        final List<Object> args = new ArrayList<>();
        paramValues.forEach((param, value) -> {
            if (paramNameToFieldMap.containsKey(param)) {
                // Inject fields in object with paramValues
                try {
                    paramNameToFieldMap.get(param).set(instance, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (paramNameToAnnotatedTypeMap.containsKey(param)) {
                // Inject method parameters
                AnnotatedType type = paramNameToAnnotatedTypeMap.get(param);
                Type typeCastedValue = type.getType().getClass().cast(value);
                // TODO check if both directly adding and typecasting works, and if the order of parameters should be found bforehand
                args.add(typeCastedValue);
            }
        });
        // Instantiate classes, do all stuff, and invoke method
        destinationMethod.invoke(instance, args);
    }
}
