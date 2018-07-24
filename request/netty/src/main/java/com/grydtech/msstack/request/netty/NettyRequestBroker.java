package com.grydtech.msstack.request.netty;

import com.grydtech.msstack.core.annotation.FrameworkComponent;
import com.grydtech.msstack.core.component.RequestBroker;
import com.grydtech.msstack.request.netty.routing.Router;

@SuppressWarnings("unused")
@FrameworkComponent
public class NettyRequestBroker extends RequestBroker {

    @Override
    public void start() throws Exception {
        final Router router = Router.build(getClasses());
        final NettyServer server = new NettyServer(applicationConfiguration.getServer().getPort())
                .setRouter(router)
                .build();
        server.run();
    }
}
