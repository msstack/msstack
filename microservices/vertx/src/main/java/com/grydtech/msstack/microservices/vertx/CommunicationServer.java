package com.grydtech.msstack.microservices.vertx;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

public class CommunicationServer<Request, Response> {

	private static final String LOCALHOST = "localhost";

	private static final int SERVERPORT = 8080;

	public CommunicationServer() {
		Vertx vertx = Vertx.vertx();

		Handler<HttpServerRequest> httpRequestHandler = request -> {
			HttpServerResponse response = request.response();
			response.putHeader("Content-Type", "text/plain");
			response.write("some text");
			response.end();
		};

		HttpServer httpServer = vertx.createHttpServer().requestHandler(httpRequestHandler).listen(SERVERPORT, LOCALHOST);
	}
}
