package com.grydtech.msstack.microservices.netty;

import com.grydtech.msstack.core.MicroserviceApplication;
import com.grydtech.msstack.microservices.netty.routing.Router;

public class NettyApplication extends MicroserviceApplication {

    @Override
    public void start() throws Exception {
        final int port = getPort();
        final NettyServer server = new NettyServer(port).setRouter(Router.build(this.getHandlers())).build();
        server.run();
    }
}
