package com.grydtech.msstack.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.grydtech.msstack.core.EventBusAdapter;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventBus {

	Class<? extends EventBusAdapter> value();
}
