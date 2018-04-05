package com.grydtech.msstack.core;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public interface QueryHandler<R extends Request, S extends Response> extends GenericHandler<R, S> {

	@Override
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	S handle(R request);
}
