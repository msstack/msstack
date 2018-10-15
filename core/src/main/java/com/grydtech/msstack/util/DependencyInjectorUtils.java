package com.grydtech.msstack.util;

import com.grydtech.msstack.annotation.FrameworkComponent;
import com.grydtech.msstack.annotation.InjectInstance;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Logger;

public final class DependencyInjectorUtils {

    private static final Logger LOGGER = Logger.getLogger(DependencyInjectorUtils.class.toGenericString());
    private static final ClassPathScanner scanner = new ClassPathScanner("com.grydtech.msstack");

    private DependencyInjectorUtils() {

    }

    /**
     * Assume @InjectInstance only annotates static fields in Class
     *
     * @param field Field to inject instance into
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
     * Automatically inject properties annotated with @InjectInstance
     * in classes annotated with @FrameworkComponent
     * with first occurrence from classpath
     */
    public static void inject()
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
