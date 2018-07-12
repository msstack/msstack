package com.grydtech.msstack.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for a ServerComponent.
 * A server has a <b>host</b> and a <b>port</b>
 * This provides the ability to run services on different hosts/ports
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@FrameworkComponent
public @interface ServerComponent {

    int port() default 8080;

    String host() default "127.0.0.1";
}
