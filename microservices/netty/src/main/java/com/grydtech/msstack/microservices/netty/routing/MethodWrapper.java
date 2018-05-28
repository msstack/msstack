package com.grydtech.msstack.microservices.netty.routing;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is a wrapper for {@link Method} Class, and is used to automatically instantiate objects and injectParameters
 * PathParams and QueryParams before invoking the method.
 * TODO add support for HeaderParam and MatrixParam as well
 */
public final class MethodWrapper {

    private static final Logger LOGGER = Logger.getLogger(MethodWrapper.class.toGenericString());

    /**
     * The {@link Method} that is invoked with route params
     */
    private final Method destinationMethod;

    /**
     * Mapping from Param ({@link PathParam} or {@link QueryParam}) name to the {@link Field} annotated with it
     */
    private final Map<String, Field> classFieldMap;


    /**
     * Type of Class that the Method expects
     */
    private final Class<?> argumentClass;

    /**
     * Constructor for {@link MethodWrapper}
     *
     * @param destinationMethod The method to execute when invoking the MethodWrapper
     */
    protected MethodWrapper(Method destinationMethod) {
        this.destinationMethod = destinationMethod;
        this.argumentClass = destinationMethod.getParameterTypes()[0];
        classFieldMap = new HashMap<>();
        this.buildFieldMap();
    }

    /**
     * @return Argument class that the method expects
     */
    protected Class<?> getArgumentClass() {
        return argumentClass;
    }

    /**
     * Add Fields annotated with {@link PathParam} to {@code classFieldMap}
     */
    private void buildFieldMap() {
        Arrays.stream(destinationMethod.getDeclaringClass().getDeclaredFields())
                .parallel()
                .forEach(field -> {
                    final String param;
                    if (field.isAnnotationPresent(PathParam.class)) {
                        param = field.getDeclaredAnnotation(PathParam.class).value();
                        classFieldMap.put(param, field);
                    }
                });
    }


    /**
     * Execute the wrapped {@link Method} with the given parameters
     *
     * @param dataObject The content extracted from URIs
     */
    public Object invoke(Object dataObject) {

        final Class<?> methodDeclaringClass = destinationMethod.getDeclaringClass();
        final Class<?> dataObjectClass = dataObject.getClass();

        // Declare response object
        Object response = null;

        try {
            // Instantiate the class instance
            final Object classInstance = methodDeclaringClass.newInstance();

            // For each CLASS FIELD, injectParameters available value from params
            classFieldMap.forEach((fieldName, field) -> {
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
            Object argInstance = argumentClass.newInstance();

            // Assign field values from dataObject to argInstance
            // IMPORTANT ASSUMPTION - Data Object contains the field names of Arg Object. Others are ignored
            Arrays.stream(dataObjectClass.getDeclaredFields()).parallel().forEach(field -> {
                field.setAccessible(true);
                String fieldName = field.getName();
                try {
                    Field declaredField = argumentClass.getDeclaredField(fieldName);
                    declaredField.setAccessible(true);
                    declaredField.set(argInstance, field.get(dataObject));
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    LOGGER.log(Level.WARNING, e.getMessage(), e);
                }
            });

            // Invoke the method on instance, with args
            response = destinationMethod.invoke(classInstance, argInstance);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return response;
    }
}
