package com.grydtech.msstack.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a property which is automatically injected on class loading.
 * The property class should be annotated with {@link FrameworkComponent} for this to work.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoInjected {
}
