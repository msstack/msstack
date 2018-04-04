package com.grydtech.msstack.core;

import javax.ws.rs.POST;

public interface CommandHandler<R extends Request, S extends Response> extends Handler<R, S> {

	@Override
	@POST
	S handle(R request);
}
