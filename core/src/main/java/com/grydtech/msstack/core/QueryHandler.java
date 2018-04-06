package com.grydtech.msstack.core;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public interface QueryHandler<R extends com.grydtech.msstack.core.Request, S extends com.grydtech.msstack.core.Response> extends GenericHandler<R, S> {

    @Override
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    S handle(R request);
}
