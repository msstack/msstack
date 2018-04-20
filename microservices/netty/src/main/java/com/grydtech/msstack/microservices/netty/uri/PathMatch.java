package com.grydtech.msstack.microservices.netty.uri;

import com.sun.istack.internal.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PathMatch {
    private String path;
    private Map<String, String> paramMatches = new HashMap<>();

    public String getPath() {
        return path;
    }

    public PathMatch setPath(String path) {
        this.path = path;
        return this;
    }

    @NotNull
    public Map<String, String> getParamMatches() {
        return paramMatches;
    }

    public PathMatch setParamMatches(@NotNull Map<String, String> paramMatches) {
        this.paramMatches = paramMatches;
        return this;
    }
}
