package com.grydtech.msstack.core;

import javax.ws.rs.GET;

public interface QueryHandler<R extends Request, S extends Response> extends Handler<R, S> {

	@Override
	@GET
	S handle(R request);
}
