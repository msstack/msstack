package com.grydtech.msstack.request.netty;

import com.grydtech.msstack.request.netty.pipeline.FullHttpRequestHandler;
import com.grydtech.msstack.request.netty.routing.Router;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

final class NettyServer {

    private int port;

    private NioEventLoopGroup workerGroup;

    private ServerBootstrap serverBootstrap;

    private Router router;

    protected NettyServer(int port) {
        this.port = port;
    }

    protected NettyServer build() {
        workerGroup = new NioEventLoopGroup();

        serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(getChannelHandler());
        return this;
    }

    protected void run() throws InterruptedException {
        try {
            // Bind and run to accept incoming connections
            ChannelFuture f = serverBootstrap.bind(port).sync();
            // Block thread until channel closed
            f.channel().closeFuture().sync();
        } finally {
            // Gracefully shutdown server after thread resumes
            workerGroup.shutdownGracefully();
        }
    }

    public NettyServer setRouter(Router router) {
        this.router = router;
        return this;
    }

    private ChannelHandler getChannelHandler() {
        return new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("codec", new HttpServerCodec());
                pipeline.addLast("aggregator", new HttpObjectAggregator(1048576));
                pipeline.addLast("handler", new FullHttpRequestHandler(router));
            }
        };
    }
}
