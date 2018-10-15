package com.grydtech.msstack.util;

import com.grydtech.msstack.annotation.FrameworkComponent;
import com.grydtech.msstack.annotation.InjectConfiguration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class ConfigurationInjectorUtils {
    private static final Logger LOGGER = Logger.getLogger(ConfigurationInjectorUtils.class.toGenericString());
    private static final ClassPathScanner scanner = new ClassPathScanner("com.grydtech.msstack");
    private final static Map<String, Object> keyValueMap = new HashMap<>();

    private ConfigurationInjectorUtils() {
    }

    /**
     * Assume @InjectConfiguration only annotates static fields in Class
     *
     * @param field Field to inject instance into
     * @throws IllegalAccessException The field cannot be accessed
     */
    private static void injectInstanceIntoField(Field field) throws IllegalAccessException {
        boolean isFieldAccessible = field.isAccessible();
        if (!isFieldAccessible) {
            field.setAccessible(true);
        }
        // Inject into static fields
        String objectKey = field.getType().getSimpleName();
        if (Modifier.isStatic(field.getModifiers()) && keyValueMap.containsKey(objectKey)) {
            field.set(null, keyValueMap.get(objectKey));
        }
        field.setAccessible(isFieldAccessible);
        LOGGER.info(String.format("Injected value - %s", field.getName()));
    }

    /**
     * Register values to be injected
     *
     * @param key    key used when injecting instance
     * @param object value object
     */
    public static void putValueObject(String key, Object object) {
        keyValueMap.put(key, object);
    }

    /**
     * Automatically inject properties annotated with @InjectConfiguration
     * in classes annotated with @FrameworkComponent
     * with saved value in keyValueMap
     */
    public static void inject() throws IllegalAccessException {
        for (Class aClass : scanner.getTypesAnnotatedWith(FrameworkComponent.class)) {
            for (Field field : aClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(InjectConfiguration.class)) {
                    injectInstanceIntoField(field);
                }
            }
        }
    }
}
