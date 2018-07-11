package com.grydtech.msstack.request.jaxrs.features;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class JacksonFeature implements Feature {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider() {{
        setMapper(mapper);
    }};

    @Override
    public boolean configure(FeatureContext featureContext) {
        featureContext.register(provider);
        return true;
    }
}
