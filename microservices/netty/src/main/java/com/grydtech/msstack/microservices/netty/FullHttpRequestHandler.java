package com.grydtech.msstack.microservices.netty;

import com.grydtech.msstack.core.Response;
import com.grydtech.msstack.microservices.netty.routing.Router;
import com.grydtech.msstack.microservices.netty.routing.RoutingResult;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class FullHttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final Router httpRouter;

    protected FullHttpRequestHandler(Router router) {
        this.httpRouter = router;
    }


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

        // Route and execute
        RoutingResult routeResult = httpRouter.route(httpMethod, path);
        Response response = null;
        Map<String, List<String>> args = new HashMap<>();
        if (routeResult != null) {
            args.putAll(routeResult.getPathMatch().getParamMatches());
            args.putAll(queryParameters);
            response = (Response) routeResult.getMethod().invoke(args);
        }

        // Set response parameters
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
        // Write response as HTTP content
        if (response != null) {
            httpResponse.content().writeBytes(response.toString().getBytes());
        }

        // Write Response to Channel Context
        ctx.writeAndFlush(httpResponse);
    }
}
