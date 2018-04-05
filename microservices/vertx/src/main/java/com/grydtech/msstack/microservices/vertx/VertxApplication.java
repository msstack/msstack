package com.grydtech.msstack.microservices.vertx;

import com.grydtech.msstack.core.MicroserviceApplication;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

public class VertxApplication extends MicroserviceApplication {

	@Override
	public void start() throws Exception {
		Vertx vertx = Vertx.vertx();

		Handler<HttpServerRequest> httpRequestHandler = request -> {
			HttpServerResponse response = request.response();
			response.putHeader("Content-Type", "text/plain");
			response.write("some text");
			response.end();
		};

		HttpServer httpServer = vertx.createHttpServer().requestHandler(httpRequestHandler).listen(getPort(), getHost());
	}
}
