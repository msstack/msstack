package com.grydtech.msstack.core;

public interface GenericHandler<R, S> {

    S handle(R request);
}
