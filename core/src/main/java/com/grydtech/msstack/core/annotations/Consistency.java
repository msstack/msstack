package com.grydtech.msstack.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Consistency {

	ConsistencyPolicy value();
}
