package com.grydtech.msstack.microservices.netty;

import java.util.List;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;

final class FullHttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
		final QueryStringDecoder queryStringDecoder;
		final HttpMethod httpMethod;
		final String path;
		final Map<String, List<String>> queryParameters;

		// Get Request Parameters
		httpMethod = request.method();
		queryStringDecoder = new QueryStringDecoder(request.uri());
		path = queryStringDecoder.path();
		queryParameters = queryStringDecoder.parameters();

		// Set response parameters
		HttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);

		// Write Response to Channel Context
		ctx.writeAndFlush(httpResponse);
	}
}
