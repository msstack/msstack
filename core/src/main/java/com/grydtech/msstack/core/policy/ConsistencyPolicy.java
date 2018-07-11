package com.grydtech.msstack.core.policy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("unused")
public @interface ConsistencyPolicy {

    Policy value();

    enum Policy {
        IMMEDIATE,
        EVENTUAL
    }
}
