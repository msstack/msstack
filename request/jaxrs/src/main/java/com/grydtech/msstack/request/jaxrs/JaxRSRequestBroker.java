package com.grydtech.msstack.request.jaxrs;

import com.grydtech.msstack.core.annotation.FrameworkComponent;
import com.grydtech.msstack.core.component.RequestBroker;
import com.grydtech.msstack.request.jaxrs.features.JacksonFeature;

@FrameworkComponent
public abstract class JaxRSRequestBroker extends RequestBroker {

    @Override
    public void start() throws Exception {
        getClasses().add(JacksonFeature.class);
    }

}
