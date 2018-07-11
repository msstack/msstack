package com.grydtech.msstack.request.netty.pipeline;

import com.grydtech.msstack.exception.RouteNotFoundException;
import com.grydtech.msstack.request.netty.routing.Router;
import com.grydtech.msstack.request.netty.routing.RoutingResult;
import com.grydtech.msstack.request.netty.util.InjectionUtils;
import com.grydtech.msstack.util.JsonConverter;
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

public final class FullHttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final Router httpRouter;

    public FullHttpRequestHandler(Router router) {
        this.httpRouter = router;
    }

    @Override
    protected final void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request)
            throws IllegalAccessException, InstantiationException, RouteNotFoundException {
        final HttpMethod httpMethod = request.method();
        final QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
        Object methodResult = null;

        // Find destination method
        RoutingResult routeResult = httpRouter.route(httpMethod, queryStringDecoder.path());

        // Cast Payload to Object
        String payloadStr = request.content().toString(Charset.defaultCharset());
        Class<?> argClass = routeResult.getArgClass();
        Object argObject = payloadStr.isEmpty()
                ? argClass.newInstance()
                : JsonConverter.getObject(payloadStr, argClass);

        if (argObject != null) {
            // AutoInjected query parameters and path parameters to targetObject
            InjectionUtils.injectParameters(argObject, queryStringDecoder.parameters());
            InjectionUtils.injectParameters(argObject, routeResult.getPathParameters());
            // Invoke method with argObject, and get the result
            methodResult = routeResult.getMethod().invoke(argObject);
        }

        // Set response parameters
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

        // Write response as HTTP content
        if (methodResult != null) {
            String responsePayload = JsonConverter.toJsonString(methodResult);
            if (responsePayload != null) {
                byte[] responsePayloadBytes = responsePayload.getBytes(Charset.defaultCharset());
                httpResponse.content().clear().writeBytes(responsePayloadBytes);
                httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, responsePayloadBytes.length);
            }
        } else {
            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, 0);
        }

        // Write Response to Channel Context
        ctx.writeAndFlush(httpResponse);
    }
}
