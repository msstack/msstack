package com.grydtech.msstack.request.jaxrs;

import com.grydtech.msstack.core.annotation.FrameworkComponent;
import com.grydtech.msstack.core.annotation.Server;
import com.grydtech.msstack.core.component.RequestBroker;
import com.grydtech.msstack.request.jaxrs.features.JacksonFeature;

@FrameworkComponent
@Server
public abstract class JaxRSRequestBroker extends RequestBroker {

    public JaxRSRequestBroker() {
        getClasses().add(JacksonFeature.class);
    }
}
