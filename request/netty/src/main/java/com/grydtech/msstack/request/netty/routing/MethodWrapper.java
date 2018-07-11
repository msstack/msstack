package com.grydtech.msstack.request.netty.routing;

import javax.ws.rs.PathParam;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is a wrapper for {@link Method} Class.
 * It's used to instantiate class instances and inject parameters before invoking the method.
 */
public final class MethodWrapper {

    // TODO add support for HeaderParam and MatrixParam as well

    private static final Logger LOGGER = Logger.getLogger(MethodWrapper.class.toGenericString());

    /**
     * The {@link Method} that is invoked with route params
     */
    private final Method method;

    /**
     * Mapping from ParamId to annotated {@link Field}
     */
    private final Map<String, Field> fieldMap;


    /**
     * Type of Class that the Method expects
     */
    private final Class<?> argClass;

    /**
     * Constructor for {@link MethodWrapper}
     *
     * @param method The method to execute when invoking the MethodWrapper
     */
    public MethodWrapper(Method method) {
        this.method = method;
        this.argClass = method.getParameterTypes()[0];
        fieldMap = new HashMap<>();
        this.buildFieldMap();
    }

    /**
     * @return Argument class that the method expects
     */
    protected final Class<?> getArgClass() {
        return argClass;
    }

    /**
     * Add Fields annotated with {@link PathParam} to {@code fieldMap}
     */
    private void buildFieldMap() {
        Arrays.stream(method.getDeclaringClass().getDeclaredFields())
                .parallel()
                .forEach(field -> {
                    final String param;
                    if (field.isAnnotationPresent(PathParam.class)) {
                        param = field.getDeclaredAnnotation(PathParam.class).value();
                        fieldMap.put(param, field);
                    }
                });
    }


    /**
     * Execute the wrapped {@link Method} with the given parameters
     *
     * @param dataObject The content extracted from URIs
     */
    public final Object invoke(Object dataObject) {

        final Class<?> methodDeclaringClass = method.getDeclaringClass();
        final Class<?> dataObjectClass = dataObject.getClass();

        // Declare response object
        Object response = null;

        try {
            // Instantiate the class instance
            final Object classInstance = methodDeclaringClass.newInstance();

            // For each CLASS FIELD, injectParameters available value from params
            fieldMap.forEach((fieldName, field) -> {
                try {
                    field.setAccessible(true);
                    // Find field with name 's' from the content object, and assign to destination object
                    Field declaredField = dataObjectClass.getDeclaredField(fieldName);
                    declaredField.setAccessible(true);
                    Object dataValue = declaredField.get(dataObject);
                    field.set(classInstance, dataValue);
                } catch (IllegalAccessException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                } catch (NoSuchFieldException e) {
                    LOGGER.log(Level.WARNING, e.getMessage(), e);
                }
            });
            // TODO after instantiation and injecting fields, cache it to increase the performance of future requests

            // For each argument in method, injectParameters either the value from params or null
            // Method has only one parameter, and that parameter contains all variables.
            // Create an instance of that object, and injectParameters all available fields with values from map
            Object argInstance = argClass.newInstance();

            // Assign field values from dataObject to argInstance
            // IMPORTANT ASSUMPTION - Data Object contains the field names of Arg Object. Others are ignored
            Arrays.stream(dataObjectClass.getDeclaredFields()).parallel().forEach(field -> {
                field.setAccessible(true);
                String fieldName = field.getName();
                try {
                    Field declaredField = argClass.getDeclaredField(fieldName);
                    declaredField.setAccessible(true);
                    declaredField.set(argInstance, field.get(dataObject));
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    LOGGER.log(Level.WARNING, e.getMessage(), e);
                }
            });

            // Invoke the method on instance, with args
            response = method.invoke(classInstance, argInstance);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return response;
    }
}
