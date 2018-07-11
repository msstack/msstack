package com.grydtech.msstack.util;

import com.grydtech.msstack.core.annotation.AutoInjected;
import com.grydtech.msstack.core.annotation.FrameworkComponent;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class DependencyInjector {

    private static final ClassPathScanner scanner = new ClassPathScanner("com.grydtech.msstack");

    private DependencyInjector() {

    }

    /**
     * Assume @AutoInjected only annotates static fields in Class
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
        System.out.printf("Injected - %s", field.getType().toGenericString());
    }

    /**
     * Automatically inject properties annotated with @AutoInjected
     * in classes annotated with @FrameworkComponent
     * with first occurrence from classpath
     */
    public static void inject()
            throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        for (Class aClass : scanner.getTypesAnnotatedWith(FrameworkComponent.class)) {
            for (Field field : aClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(AutoInjected.class)) {
                    injectInstanceIntoField(field);
                }
            }
        }
    }
}
