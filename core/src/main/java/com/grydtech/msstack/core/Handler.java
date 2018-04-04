package com.grydtech.msstack.core;

public interface Handler<R, S> {

	S handle(R request);
}
