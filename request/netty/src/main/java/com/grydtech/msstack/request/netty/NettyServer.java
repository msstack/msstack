package com.grydtech.msstack.request.netty;

import com.grydtech.msstack.request.netty.pipeline.FullHttpRequestHandler;
import com.grydtech.msstack.request.netty.routing.Router;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

final class NettyServer {

    private int port;

    private NioEventLoopGroup eventLoopGroup;

    private ServerBootstrap serverBootstrap;

    private Router router;

    protected NettyServer(int port) {
        this.port = port;
    }

    protected NettyServer build() {
        eventLoopGroup = new NioEventLoopGroup();
        serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(eventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(getHttpServerInitializer());
        return this;
    }

    protected void run() throws InterruptedException {
        try {
            // Bind and start to accept incoming connections
            Channel ch = serverBootstrap.bind(port).sync().channel();
            ch.closeFuture().sync();
        } finally {
            // Gracefully shutdown server after thread resumes
            eventLoopGroup.shutdownGracefully();
        }
    }

    public NettyServer setRouter(Router router) {
        this.router = router;
        return this;
    }

    private ChannelHandler getHttpServerInitializer() {
        return new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new HttpServerCodec());
                pipeline.addLast(new HttpObjectAggregator(64 * 1024, true));
                pipeline.addLast(new FullHttpRequestHandler(router));
            }
        };
    }
}
