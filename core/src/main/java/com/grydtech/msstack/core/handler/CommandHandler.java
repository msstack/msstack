package com.grydtech.msstack.core.handler;

import javax.ws.rs.POST;

@SuppressWarnings("unused")
public interface CommandHandler<R, S> extends RequestHandler<R, S> {

    @POST
    @Override
    S handle(R request);
}
