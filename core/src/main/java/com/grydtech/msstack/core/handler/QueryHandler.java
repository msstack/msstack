package com.grydtech.msstack.core.handler;

import javax.ws.rs.GET;

@SuppressWarnings("unused")
public interface QueryHandler<R, S> extends RequestHandler<R, S> {

    @GET
    @Override
    S handle(R request);
}
