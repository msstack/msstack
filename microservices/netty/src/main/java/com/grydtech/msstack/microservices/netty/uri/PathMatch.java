package com.grydtech.msstack.microservices.netty.uri;

import com.sun.istack.internal.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathMatch {
    private Map<String, List<String>> paramMatches = new HashMap<>();

    @NotNull
    public Map<String, List<String>> getParamMatches() {
        return paramMatches;
    }

    PathMatch setMatchedParams(@NotNull Map<String, List<String>> paramMatches) {
        this.paramMatches = paramMatches;
        return this;
    }
}
