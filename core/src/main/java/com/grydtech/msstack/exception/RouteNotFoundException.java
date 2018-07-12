package com.grydtech.msstack.exception;

import com.grydtech.msstack.core.annotation.ResponseStatus;

@SuppressWarnings("unused")
@ResponseStatus(value = 404, message = "Route not found")
public class RouteNotFoundException extends Exception {
}
