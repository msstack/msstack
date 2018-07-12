package com.grydtech.msstack.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Treats a class as a framework component in MSStack.
 * Classes annotated with {@link FrameworkComponent} supports runtime dependency injection
 * for declared fields with {@link AutoInjected} annotation.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FrameworkComponent {

}
