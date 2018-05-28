package com.grydtech.msstack.microservices.netty;

import com.grydtech.msstack.common.util.JsonConverter;
import com.grydtech.msstack.microservices.netty.routing.Router;
import com.grydtech.msstack.microservices.netty.routing.RoutingResult;
import com.grydtech.msstack.microservices.netty.util.Injector;
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

import java.nio.charset.Charset;

final class FullHttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final Router httpRouter;

    protected FullHttpRequestHandler(Router router) {
        this.httpRouter = router;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws IllegalAccessException, InstantiationException {

        final QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
        final HttpMethod httpMethod = request.method();
        final String path = queryStringDecoder.path();
        Object methodResult = null;

        // Find destination method
        RoutingResult routeResult = httpRouter.route(httpMethod, path);
        if (routeResult != null) {
            // Cast Payload to Object
            String requestPayload = request.content().toString(Charset.defaultCharset());
            Class<?> argClass = routeResult.getArgumentClass();
            Object argObject = requestPayload.isEmpty()
                    ? argClass.newInstance()
                    : JsonConverter.getObject(requestPayload, argClass);

            if (argObject != null) {
                // Inject query parameters and path parameters to targetObject
                Injector.injectParameters(argObject, queryStringDecoder.parameters());
                Injector.injectParameters(argObject, routeResult.getPathParameters());
                // Invoke method with argObject, and get the result
                methodResult = routeResult.getMethod().invoke(argObject);
            }
        }

        // Set response parameters
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);

        // Write response as HTTP content
        if (methodResult != null) {
            String responsePayload = JsonConverter.toJsonString(methodResult);
            httpResponse.content().writeCharSequence(responsePayload, Charset.defaultCharset());
        }

        // Write Response to Channel Context
        ctx.writeAndFlush(httpResponse);
    }
}
