package com.grydtech.msstack.util;

import com.grydtech.msstack.core.annotation.FrameworkComponent;
import com.grydtech.msstack.core.annotation.Value;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class ValueInjectorUtils {
    private static final Logger LOGGER = Logger.getLogger(ValueInjectorUtils.class.toGenericString());
    private static final ClassPathScanner scanner = new ClassPathScanner("com.grydtech.msstack");
    private final static Map<String, Object> valueObjectMap = new HashMap<>();

    private ValueInjectorUtils() {
    }

    /**
     * Assume @Value only annotates static fields in Class
     *
     * @param field Field to inject instance into
     * @throws IllegalAccessException The field cannot be accessed
     */
    private static void injectInstanceIntoField(String key, Field field) throws IllegalAccessException {
        boolean isFieldAccessible = field.isAccessible();
        if (!isFieldAccessible) {
            field.setAccessible(true);
        }
        // Inject into static fields
        String objectKey = key.equals("") ? field.getName() : key;
        if (Modifier.isStatic(field.getModifiers()) && valueObjectMap.containsKey(objectKey)) {
            field.set(null, valueObjectMap.get(objectKey));
        }
        field.setAccessible(isFieldAccessible);
        LOGGER.info(String.format("Injected value - %s", field.getName()));
    }

    /**
     * Register values to be injected
     * @param key key used when injecting instance
     * @param object value object
     */
    public static void putValueObject(String key, Object object) {
        valueObjectMap.put(key, object);
    }

    /**
     * Automatically inject properties annotated with @Value
     * in classes annotated with @FrameworkComponent
     * with saved value in valueObjectMap
     */
    public static void inject() throws IllegalAccessException {
        for (Class aClass : scanner.getTypesAnnotatedWith(FrameworkComponent.class)) {
            for (Field field : aClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Value.class)) {
                    Value value = field.getAnnotation(Value.class);
                    String key = value.value();
                    injectInstanceIntoField(key, field);
                }
            }
        }
    }
}
