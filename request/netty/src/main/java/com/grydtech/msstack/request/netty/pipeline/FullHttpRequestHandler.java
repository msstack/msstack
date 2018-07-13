package com.grydtech.msstack.request.netty.pipeline;

import com.grydtech.msstack.core.annotation.ResponseStatus;
import com.grydtech.msstack.exception.RouteNotFoundException;
import com.grydtech.msstack.request.netty.routing.Router;
import com.grydtech.msstack.request.netty.routing.RoutingResult;
import com.grydtech.msstack.request.netty.util.InjectionUtils;
import com.grydtech.msstack.util.JsonConverter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public final class FullHttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static Logger LOGGER = Logger.getLogger(FullHttpRequestHandler.class.getSimpleName());
    private final Router httpRouter;

    public FullHttpRequestHandler(Router router) {
        this.httpRouter = router;
    }

    @Override
    protected final void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {

        // Find the HTTP method and URI
        final HttpMethod httpMethod = request.method();
        final QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        final String path = decoder.path();
        final DefaultFullHttpResponse response;

        // Get Routing Result
        RoutingResult routeResult;
        try {
            routeResult = httpRouter.route(httpMethod, path);
        } catch (RouteNotFoundException e) {
            // If Routing Result not found, return a HTTP Response indicating this
            ResponseStatus status = e.getClass().getAnnotation(ResponseStatus.class);
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(status.value()));
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            // Put error message in response body as JSON
            Map<String, String> message = new HashMap<>();
            message.put("error", status.message());
            String payload = JsonConverter.toJsonString(message).orElse("");
            response.content().writeBytes(payload.getBytes(Charset.defaultCharset()));
            // Write response
            ctx.write(response);
            return;
        }

        // Extract payload from request in the class type given by routeResult
        final Object payload;
        try {
            payload = extractPayload(request.content(), routeResult.getArgClass());
        } catch (InstantiationException | IllegalAccessException e) {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            ctx.write(response);
            return;
        }

        // Inject parameters into payload, execute method, and get the result
        this.injectFields(payload, routeResult.getPathParameters(), decoder.parameters());
        Object methodResult = routeResult.getMethod().invoke(payload);
        Optional<String> payloadStr = JsonConverter.toJsonString(methodResult);

        // TODO implement a way to get STATUS_CODE from methodResult as well
        response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

        // Prepare HTTP Response Payload
        if (payloadStr.isPresent()) {
            byte[] responsePayloadBytes = payloadStr.get().getBytes(Charset.defaultCharset());
            response.content().clear().writeBytes(responsePayloadBytes);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        } else {
            // Send a no content response
            response.content().clear();
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        }

        // Write Response to Channel Context
        ctx.write(response);
    }

    private void injectFields(Object payload, Map<String, String> pathParameters, Map<String, List<String>> queryParameters) {
        InjectionUtils.injectParameters(payload, pathParameters);
        InjectionUtils.injectParameters(payload, queryParameters);
    }

    private <T> T extractPayload(ByteBuf content, Class<T> ofType) throws InstantiationException, IllegalAccessException {
        String payloadStr = content.toString(Charset.defaultCharset());
        return payloadStr.isEmpty()
                ? ofType.newInstance()
                : JsonConverter.getObject(payloadStr, ofType).orElseThrow(ClassCastException::new);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.info(cause.getLocalizedMessage());
        ctx.close();
    }
}
