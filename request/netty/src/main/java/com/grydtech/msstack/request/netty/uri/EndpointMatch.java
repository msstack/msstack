package com.grydtech.msstack.request.netty.uri;

import java.util.Map;

/**
 * This class wraps the {@link javax.ws.rs.PathParam} matches with their respective {@link String} value
 */
public class EndpointMatch {

    private final Map<String, String> parameters;

    public EndpointMatch(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
