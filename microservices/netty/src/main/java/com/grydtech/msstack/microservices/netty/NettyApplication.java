package com.grydtech.msstack.microservices.netty;

import com.grydtech.msstack.core.MicroserviceApplication;

public class NettyApplication extends MicroserviceApplication {

	public static void main(String[] args) throws Exception {
		new NettyApplication().start();
	}

	@Override
	public void start() throws Exception {
		final int port = getPort();
		final NettyServer nettyServer = new NettyServer(port).init();
		nettyServer.run();
	}
}
