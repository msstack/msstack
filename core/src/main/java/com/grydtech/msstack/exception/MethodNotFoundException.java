package com.grydtech.msstack.exception;

import com.grydtech.msstack.core.annotation.ResponseStatus;

@SuppressWarnings("unused")
@ResponseStatus(value = 500, message = "Method not Found")
public class MethodNotFoundException extends Exception {
}
