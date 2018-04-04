package com.grydtech.msstack.core.annotations;

public @interface Microservice {
    String port() default "8080";
    String host() default "http://localhost";
}
