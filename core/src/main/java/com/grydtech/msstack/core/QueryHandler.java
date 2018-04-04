package com.grydtech.msstack.core;

import javax.ws.rs.GET;

public interface QueryHandler<R extends Request, S extends Response> extends GenericHandler<R, S> {

	@Override
	@GET
	S handle(R request);
}
