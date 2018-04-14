package com.grydtech.msstack.microservices.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
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

	private NioEventLoopGroup incomingEventLoopGroup, workerEventLoopGroup;

	private ServerBootstrap serverBootstrap;

	NettyServer(int port) {
		this.port = port;
	}

	NettyServer init() {
		incomingEventLoopGroup = new NioEventLoopGroup(1);
		workerEventLoopGroup = new NioEventLoopGroup(100);

		serverBootstrap = new ServerBootstrap();

		serverBootstrap.group(incomingEventLoopGroup, workerEventLoopGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					public void initChannel(SocketChannel ch) {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast("codec", new HttpServerCodec());
						pipeline.addLast("aggregator", new HttpObjectAggregator(1048576));
						pipeline.addLast("handler", new FullHttpRequestHandler());
					}
				})
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);

		return this;
	}

	void run() throws InterruptedException {
		try {
			// Bind and start to accept incoming connections
			ChannelFuture f = serverBootstrap.bind(port).sync();
			// Block thread until channel closed
			f.channel().closeFuture().sync();
		}
		finally {
			// Gracefully shutdown server after thread resumes
			workerEventLoopGroup.shutdownGracefully();
			incomingEventLoopGroup.shutdownGracefully();
		}
	}
}
