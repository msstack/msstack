package com.grydtech.msstack.util;

import com.grydtech.msstack.annotation.FrameworkComponent;
import com.grydtech.msstack.annotation.InjectInstance;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Logger;

/**
 * Dependency Injection Utility Class to manage internal dependencies
 */
public final class DIUtils {

    private static final Logger LOGGER;
    @Getter
    private static final ClassPathScanner scanner;

    static {
        LOGGER = Logger.getLogger(DIUtils.class.getName());
        scanner = new ClassPathScanner("com.grydtech.msstack");
    }

    private DIUtils() {
    }

    /**
     * Assume @InjectInstance only annotates static fields in Class
     *
     * @param field Field to resolveAll instance into
     * @throws ClassNotFoundException No Implementation of the annotated class found in classpath
     * @throws IllegalAccessException The field cannot be accessed
     * @throws InstantiationException The class cannot be instantiated
     */
    private static void injectInstanceIntoField(Field field)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        boolean isFieldAccessible = field.isAccessible();
        if (!isFieldAccessible) {
            field.setAccessible(true);
        }
        Object implementedClassInstance = scanner.getInstance(field.getType());
        // Inject into static fields
        if (Modifier.isStatic(field.getModifiers())) {
            field.set(null, implementedClassInstance);
        }
        field.setAccessible(isFieldAccessible);
        LOGGER.info(String.format("Injected - %s", field.getType().getName()));
    }

    /**
     * Automatically inject the first match in classpath to each property annotated
     * with @InjectInstance in classes annotated with @FrameworkComponent
     */
    public static void resolveAll()
            throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        for (Class aClass : scanner.getTypesAnnotatedWith(FrameworkComponent.class)) {
            for (Field field : aClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(InjectInstance.class)) {
                    injectInstanceIntoField(field);
                }
            }
        }
    }
}
