package com.grydtech.msstack.core;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public interface CommandHandler<R extends com.grydtech.msstack.core.Request, S extends com.grydtech.msstack.core.Response>
        extends GenericHandler<R, S> {

    @Override
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    S handle(R request);
}
