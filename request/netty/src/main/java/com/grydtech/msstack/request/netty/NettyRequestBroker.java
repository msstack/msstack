package com.grydtech.msstack.request.netty;

import com.grydtech.msstack.annotation.FrameworkComponent;

@SuppressWarnings("unused")
@FrameworkComponent
public class NettyRequestBroker {

// COMMENTED OUT BECAUSE INTERNAL COMMUNICATION NOW HAPPENS VIA MESSAGE BUS
//    @Override
//    public void start() throws Exception {
//        final Router router = Router.build(getClasses());
//        final NettyServer server = new NettyServer(applicationConfiguration.getServer().getPort())
//                .setRouter(router)
//                .build();
//        server.run();
//    }
}
